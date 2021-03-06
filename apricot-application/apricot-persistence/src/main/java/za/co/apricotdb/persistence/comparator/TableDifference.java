package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The difference between two Apricot Tables.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public class TableDifference implements ApricotObjectDifference<ApricotTable> {

    private ApricotTable source;
    private ApricotTable target;
    private List<ColumnDifference> columnDiffs;
    private List<ConstraintDifference> constraintDiffs;

    public TableDifference(ApricotTable source, ApricotTable target) {
        this.source = source;
        this.target = target;
        columnDiffs = new ArrayList<>();
        constraintDiffs = new ArrayList<>();
    }

    public List<ColumnDifference> getColumnDiffs() {
        return columnDiffs;
    }

    public List<ConstraintDifference> getConstraintDiffs() {
        return constraintDiffs;
    }

    @Override
    public ApricotTable getSourceObject() {
        return source;
    }

    @Override
    public ApricotTable getTargetObject() {
        return target;
    }

    @Override
    public boolean isDifferent() {
        return source == null || target == null || isColumnsDiff() || isConstraintsDiff();
    }

    private boolean isColumnsDiff() {
        for (ColumnDifference cd : columnDiffs) {
            if (cd.isDifferent()) {
                return true;
            }
        }

        return false;
    }

    private boolean isConstraintsDiff() {
        for (ConstraintDifference cd : constraintDiffs) {
            if (cd.isDifferent()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("* Table: ").append(source != null ? source.getName() : EMPTY).append("->")
                .append(target != null ? target.getName() : EMPTY);
        getDiffFlag(sb);

        for (ColumnDifference cd : columnDiffs) {
            sb.append(cd.toString());
        }

        for (ConstraintDifference cnstrd : constraintDiffs) {
            sb.append(cnstrd.toString());
        }

        return sb.toString();
    }

    public String getOrderWeight() {
        String ret = null;
        if (isDifferent()) {
            if (source == null && target != null) {
                ret = "0001" + target.getName();
            } else if (source != null && target == null) {
                ret = "0002" + source.getName();
            } else if (source != null && target != null) {
                ret = "0003" + source.getName();
            }
        } else {
            ret = "0004" + source.getName();
        }

        return ret;
    }
}
