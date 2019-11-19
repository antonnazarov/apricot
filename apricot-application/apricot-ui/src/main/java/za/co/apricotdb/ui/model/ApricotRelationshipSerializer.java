package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import za.co.apricotdb.ui.handler.ApricotEntityHandler;

/**
 * This component performs the serialisation of the new Relationship.
 * 
 * @author Anton Nazarov
 * @since 17/03/2019
 */
@Component
public class ApricotRelationshipSerializer {

    @Autowired
    TableManager tableManager;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    ApricotEntityHandler entityHandler;

    public void serializeRelationship(EditRelationshipModel model) {
        List<ApricotColumn> columns = getForeignKeyColumns(model);
        ApricotConstraint fk = new ApricotConstraint(model.getRelationshipNameProperty().getValue(),
                ConstraintType.FOREIGN_KEY, model.getChildTable());
        for (ApricotColumn c : columns) {
            ApricotColumnConstraint acc = new ApricotColumnConstraint(fk, c);
            fk.getColumns().add(acc);
        }
        if (model.getChildTable().getConstraintByName(fk.getName()) == null) {
            model.getChildTable().getConstraints().add(fk);
        }
        constraintManager.saveConstraint(fk);

        ApricotConstraint childPk = buildPrimaryKey(columns, model);
        if (childPk != null) {
            if (entityHandler.getPrimaryKey(model.getChildTable()) == null) {
                model.getChildTable().getConstraints().add(childPk);
            }
            constraintManager.saveConstraint(childPk);
        }

        ApricotConstraint pk = entityHandler.getPrimaryKey(model.getParentTable());
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
                    if (StringUtils.isNotEmpty(pkColumn.getValueLength())) {
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
            ApricotConstraint pk = entityHandler.getPrimaryKey(model.getChildTable());
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

            return pk;
        }

        return null;
    }
}
