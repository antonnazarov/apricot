package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * The difference between Apricot Constraints.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public class ConstraintDifference implements ApricotObjectDifference<ApricotConstraint> {

    private ApricotConstraint source;
    private ApricotConstraint target;

    public ConstraintDifference(ApricotConstraint source, ApricotConstraint target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public ApricotConstraint getSourceObject() {
        return source;
    }

    @Override
    public ApricotConstraint getTargetObject() {
        return target;
    }

    @Override
    public boolean isDifferent() {
        return source == null || target == null
                || isDifferent(getColumns(source.getColumns()), getColumns(target.getColumns()))
                || (source.getType() != target.getType());
    }

    private List<String> getColumns(List<ApricotColumnConstraint> cols) {
        List<String> ret = new ArrayList<>();

        for (ApricotColumnConstraint acc : cols) {
            ret.add(acc.getColumn().getName());
        }

        return ret;
    }

    private boolean isDifferent(List<String> l1, List<String> l2) {
        if (l1.size() != l2.size()) {
            return true;
        }

        for (String s : l1) {
            if (!l2.contains(s)) {
                return true;
            }
        }

        for (String s : l2) {
            if (!l1.contains(s)) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Constraint: ").append(source != null ? source.getName() : EMPTY).append("->")
                .append(target != null ? target.getName() : EMPTY);
        getDiffFlag(sb);
        
        return sb.toString();
    }
}
