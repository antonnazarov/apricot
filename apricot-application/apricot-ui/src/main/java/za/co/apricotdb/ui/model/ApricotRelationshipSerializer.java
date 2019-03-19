package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * This component performs the serialization of the new Relationship.
 * 
 * @author Anton Nazarov
 * @since 17/03/2019
 */
@Component
public class ApricotRelationshipSerializer {

    @Autowired
    ApricotRelationshipValidator relationshipValidator;

    @Autowired
    TableManager tableManager;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    RelationshipManager relationshipManager;

    public void serializeRelationship(EditRelationshipModel model) {
        List<ApricotColumn> columns = getForeignKeyColumns(model);
        ApricotConstraint fk = new ApricotConstraint(model.getRelationshipNameProperty().getValue(),
                ConstraintType.FOREIGN_KEY, model.getChildTable());
        for (ApricotColumn c : columns) {
            ApricotColumnConstraint acc = new ApricotColumnConstraint(fk, c);
            fk.getColumns().add(acc);
        }
        constraintManager.saveConstraint(fk);

        ApricotConstraint childPk = buildPrimaryKey(columns, model);
        if (childPk != null) {
            constraintManager.saveConstraint(childPk);
        }

        ApricotConstraint pk = relationshipValidator.getPrimaryKey(model.getParentTable());
        ApricotRelationship relationship = new ApricotRelationship(pk, fk);
        relationshipManager.saveRelationship(relationship);
    }

    private List<ApricotColumn> getForeignKeyColumns(EditRelationshipModel model) {
        List<ApricotColumn> columns = new ArrayList<>();
        ApricotTable table = model.getChildTable();

        int pos = table.getColumns().size();
        for (ParentChildKeyHolder h : model.getKeys()) {
            String fieldName = h.getForeignKeyField();
            if (table.getColumnByName(fieldName) != null) {
                columns.add(table.getColumnByName(fieldName));
            } else {
                pos++;
                ApricotColumn column = new ApricotColumn();
                column.setName(fieldName);
                column.setOrdinalPosition(pos);
                column.setNullable(!h.isNotNull());
                ApricotColumn pkColumn = model.getParentTable().getColumnByName(h.getPrimaryKeyField());
                if (pkColumn != null) {
                    column.setDataType(pkColumn.getDataType());
                    if (pkColumn.getValueLength() != null) {
                        column.setValueLength(pkColumn.getValueLength());
                    }
                }
                column.setTable(table);
                table.getColumns().add(column);
                columns.add(column);
            }
        }

        return columns;
    }

    private ApricotConstraint buildPrimaryKey(List<ApricotColumn> columns, EditRelationshipModel model) {
        if (model.isPkConstraint() && model.hasNewFields()) {
            List<ApricotColumn> pkColumns = new ArrayList<>();
            ApricotConstraint pk = relationshipValidator.getPrimaryKey(model.getChildTable());
            if (pk != null) {
                for (ApricotColumnConstraint acc : pk.getColumns()) {
                    pkColumns.add(acc.getColumn());
                }

                for (ApricotColumn c : columns) {
                    if (!pkColumns.contains(c)) {
                        ApricotColumnConstraint cc = new ApricotColumnConstraint(pk, c);
                        pk.getColumns().add(cc);
                    }
                }
            } else {
                // create a new Primary Key
                pk = new ApricotConstraint(model.getChildTable().getName() + "_PK", ConstraintType.PRIMARY_KEY,
                        model.getChildTable());
                for (ApricotColumn c : columns) {
                    ApricotColumnConstraint cc = new ApricotColumnConstraint(pk, c);
                    pk.getColumns().add(cc);
                }
            }
        }

        return null;
    }
}