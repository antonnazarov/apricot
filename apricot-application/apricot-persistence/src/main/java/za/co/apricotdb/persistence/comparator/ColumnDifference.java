package za.co.apricotdb.persistence.comparator;

import za.co.apricotdb.persistence.entity.ApricotColumn;

/**
 * The difference between Apricot Columns.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public class ColumnDifference implements ApricotObjectDifference<ApricotColumn> {

    private ApricotColumn source = null;
    private ApricotColumn target = null;

    public ColumnDifference(ApricotColumn source, ApricotColumn target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public ApricotColumn getSourceObject() {
        return source;
    }

    @Override
    public ApricotColumn getTargetObject() {
        return target;
    }

    @Override
    public boolean isDifferent() {
        if (source != null && target != null) {
            if (source.isNullable() != target.isNullable()) {
                return true;
            }

            if (!source.getDataType().equalsIgnoreCase(target.getDataType())) {
                return true;
            }

            if (!source.getValueLength().equalsIgnoreCase(target.getValueLength())) {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Column: ").append(source != null ? source.getName() : EMPTY).append("->")
                .append(target != null ? target.getName() : EMPTY);
        getDiffFlag(sb);
        
        return sb.toString();
    }
}
