package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.support.excel.TableWrapper;
import za.co.apricotdb.support.excel.TableWrapper.ReportRow;

/**
 * A model builder for the EditEntityModel.
 * 
 * @author Anton Nazarov
 * @since 26/02/2019
 */
@Component
public class EditEntityModelBuilder {

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    RelationshipManager relationshipManager;

    public EditEntityModel buildModel(boolean newEntity, String tableName) {
        EditEntityModel model = new EditEntityModel(newEntity);

        if (!newEntity && tableName != null) {
            readData(tableName, model);
        }

        return model;
    }

    /**
     * Read current data for the given table and populate the model.
     */
    public void readData(String tableName, EditEntityModel model) {
        ApricotTable table = tableManager.getTableByName(tableName, snapshotManager.getDefaultSnapshot());
        if (table != null) {
            model.setEntityName(table.getName());
        } else {
            return;
        }

        List<ApricotTable> ts = new ArrayList<>();
        ts.add(table);
        List<ApricotRelationship> rels = relationshipManager.getRelationshipsForTables(ts);
        TableWrapper wrapper = new TableWrapper(table, rels);
        Map<String, ReportRow> rows = wrapper.getRowMap();

        ObservableList<ApricotColumnData> columns = FXCollections.observableArrayList();
        for (ApricotColumn col : table.getColumns()) {
            ApricotColumnData cd = new ApricotColumnData(col);
            if (rows.get(col.getName()) != null) {
                ReportRow r = rows.get(col.getName());
                cd.setComment(r.getConstraints());
            }
            columns.add(cd);
        }
        model.setColumns(columns);

        List<ApricotConstraintData> constraints = model.getConstraints();
        for (ApricotConstraint c : table.getConstraints()) {
            if (c.getType() == ConstraintType.PRIMARY_KEY || c.getType() == ConstraintType.FOREIGN_KEY) {
                continue;
            }
            ApricotConstraintData constraint = new ApricotConstraintData();
            constraint.setId(c.getId());
            constraint.setConstraintType(c.getType().name());
            constraint.setConstraintName(c.getName());
            List<ApricotColumnData> constrCols = constraint.getColumns();
            for (ApricotColumnConstraint cc : c.getColumns()) {
                ApricotColumnData acd = findColumnByName(columns, cc.getColumn().getName());
                if (acd != null) {
                    constrCols.add(acd);
                }
            }
            constraints.add(constraint);
        }
    }

    private ApricotColumnData findColumnByName(ObservableList<ApricotColumnData> columns, String name) {
        for (ApricotColumnData acd : columns) {
            if (acd.getName().getValue().equals(name)) {
                return acd;
            }
        }

        return null;
    }
}
