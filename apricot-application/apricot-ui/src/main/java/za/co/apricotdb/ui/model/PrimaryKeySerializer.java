package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * This class/component handles the primary key (the new or existing one) of the
 * table during the serialisation process.
 * 
 * @author Anton Nazarov
 * @since 04/03/2019
 */
@Component
public class PrimaryKeySerializer {

    public void handlePrimaryKey(EditEntityModel model) {
        ApricotConstraint pk = getPrimaryKey(model.getTable());
        if (getPrimaryKeyColumns(model).size() > 0 && pk == null) {
            // a new PRIMARY_KEY needs to be created
            ApricotTable table = model.getTable();
            ApricotConstraint primaryKey = new ApricotConstraint("PK_" + table.getName(),
                    ConstraintType.PRIMARY_KEY, table);
            table.getConstraints().add(primaryKey);
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
}
