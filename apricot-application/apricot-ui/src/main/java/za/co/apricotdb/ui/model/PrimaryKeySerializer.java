package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * This class/component handles the primary key (the new or existing one) of the
 * table during the serialization process.
 * 
 * @author Anton Nazarov
 * @since 04/03/2019
 */
@Component
public class PrimaryKeySerializer {

    public void serializePrimaryKey(EditEntityModel model) {
        ApricotConstraint primaryKey = getPrimaryKey(model.getTable());
        if (getPrimaryKeyColumns(model).size() > 0 && primaryKey == null) {
            // a new PRIMARY_KEY needs to be created
            ApricotTable table = model.getTable();
            primaryKey = new ApricotConstraint("PK_" + table.getName(), ConstraintType.PRIMARY_KEY, table);
            table.getConstraints().add(primaryKey);
        }

        if (primaryKey != null) {
            serializePrimaryKeyColumns(primaryKey, model);
        }
    }

    private ApricotConstraint getPrimaryKey(ApricotTable table) {
        for (ApricotConstraint c : table.getConstraints()) {
            if (c.getType() == ConstraintType.PRIMARY_KEY) {
                return c;
            }
        }

        return null;
    }

    public List<ApricotColumnData> getPrimaryKeyColumns(EditEntityModel model) {
        List<ApricotColumnData> ret = new ArrayList<>();
        for (ApricotColumnData cd : model.getColumns()) {
            if (cd.getPrimaryKey().getValue()) {
                ret.add(cd);
            }
        }

        return ret;
    }

    private void serializePrimaryKeyColumns(ApricotConstraint constraint, EditEntityModel model) {
        constraint.getColumns().clear();

        int ordinalPosition = 0;
        for (ApricotColumnData columnData : getPrimaryKeyColumns(model)) {
            ApricotColumnConstraint acc = new ApricotColumnConstraint(constraint, columnData.getColumn());
            acc.setOrdinalPosition(ordinalPosition);
            constraint.getColumns().add(acc);

            ordinalPosition++;
        }
    }
}
