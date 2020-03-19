package za.co.apricotdb.support.export;

/**
 * This holder bean resolves the APRICOT_COLUMN_IN_CONSTRAINT Entity.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
public class ConstraintColumnHolder {

    private long constraintId;
    private long columnId;
    private int ordinalPosition;

    public long getConstraintId() {
        return constraintId;
    }

    public void setConstraintId(long constraintId) {
        this.constraintId = constraintId;
    }

    public long getColumnId() {
        return columnId;
    }

    public void setColumnId(long columnId) {
        this.columnId = columnId;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    @Override
    public String toString() {
        return "ConstraintColumnHolder [constraintId=" + constraintId + ", columnId=" + columnId + ", ordinalPosition="
                + ordinalPosition + "]";
    }
}
