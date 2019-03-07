package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ColumnManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * This component serialises the new or edited entity into the Apricot project
 * database.
 * 
 * @author Anton Nazarov
 * @since 03/03/2019
 */
@Component
public class ApricotEntitySerializer {

    @Autowired
    SnapshotManager snapshotManager;
    
    @Autowired
    TableManager tableManager;
    
    @Autowired
    ApricotConstraintSerializer constraintSerializer;
    
    @Autowired
    ColumnManager columnManager;

    public void serialize(EditEntityModel model) {
        updateTable(model);
        updateColumns(model);
        constraintSerializer.serialize(model);
        deleteColumns(model);
        tableManager.saveTable(model.getTable());
    }

    private ApricotTable updateTable(EditEntityModel model) {
        ApricotTable table = null;
        if (model.isNewEntity()) {
            table = new ApricotTable();
            table.setSnapshot(snapshotManager.getDefaultSnapshot());
        } else {
            table = model.getTable();
        }

        table.setName(model.getEntityName());
        model.setTable(table);

        return table;
    }

    private void updateColumns(EditEntityModel model) {
        int pos = 0;
        for (ApricotColumnData cd : model.getColumns()) {
            ApricotColumn column = null;
            if (cd.isAdded()) {
                column = model.getTable().getColumnByName(cd.getName().getValue());
                if (column == null) {
                    column = new ApricotColumn();
                    model.getTable().getColumns().add(column);
                    cd.setColumn(column);
                }
            } else {
                column = model.getTable().getColumnById(cd.getId());
            }

            column.setOrdinalPosition(pos);
            column.setName(cd.getName().getValue());
            if (!cd.getPrimaryKey().getValue()) {
                column.setNullable(cd.getNullable().getValue());
            } else {
                //  the PK cannot be nullable
                column.setNullable(false);
            }
            column.setDataType(cd.getDataType().getValue());
            column.setValueLength(cd.getValueLength().getValue());
            column.setTable(model.getTable());

            pos++;
        }
    }

    private List<ApricotColumn> getDeletedColumns(EditEntityModel model) {
        List<ApricotColumn> deleted = new ArrayList<>();

        for (ApricotColumn c : model.getTable().getColumns()) {
            if (model.getColumnByName(c.getName()) == null) {
                deleted.add(c);
            }
        }

        return deleted;
    }

    private void deleteColumns(EditEntityModel model) {
        List<ApricotColumn> deletedColumns = getDeletedColumns(model);

        for(ApricotColumn c : deletedColumns) {
            columnManager.deleteColumn(c);
        }
        
        model.getTable().getColumns().removeAll(deletedColumns);
    }
}
