package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;

@Component
public class ApricotConstraintHandler {

    public List<ApricotConstraint> getConstraintsForColumn(ApricotColumn column) {
        List<ApricotConstraint> ret = new ArrayList<>();

        ApricotTable table = column.getTable();
        for (ApricotConstraint constr : table.getConstraints()) {
            for (ApricotColumnConstraint cc : constr.getColumns()) {
                if (cc.getColumn().equals(column)) {
                    ret.add(constr);
                }
            }
        }
        
        if (ret.size() > 1) {
            sortConstraints(ret);
        }

        return ret;
    }

    public void sortConstraints(List<ApricotConstraint> constraints) {
        constraints.sort((ApricotConstraint c1, ApricotConstraint c2) -> {
            if (c1.getType().getOrder() == c2.getType().getOrder()) {
                // use ordinal position of the constraint fields
                if (c1.getColumns() != null && c1.getColumns().size() > 0 && c2.getColumns() != null
                        && c2.getColumns().size() > 0) {
                    return c1.getColumns().get(0).getColumn().getOrdinalPosition()
                            - c2.getColumns().get(0).getColumn().getOrdinalPosition();
                }
            }
            return c1.getType().getOrder() - c2.getType().getOrder();
        });
    }
}
