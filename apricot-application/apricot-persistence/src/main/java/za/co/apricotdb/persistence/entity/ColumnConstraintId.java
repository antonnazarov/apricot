package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable key for Column in Constraint many-to-many relationship.
 *
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Embeddable
public class ColumnConstraintId implements Serializable {

    private static final long serialVersionUID = 3787111173371259780L;

    @Column(name = "constraint_id")
    private long constraintId;

    @Column(name = "column_id")
    private long columnId;

    private ColumnConstraintId() {
    }

    public ColumnConstraintId(long constraintId, long columnId) {
        this.constraintId = constraintId;
        this.columnId = columnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ColumnConstraintId that = (ColumnConstraintId) o;
        return Objects.equals(constraintId, that.constraintId)
                && Objects.equals(columnId, that.columnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraintId, columnId);
    }

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
}
