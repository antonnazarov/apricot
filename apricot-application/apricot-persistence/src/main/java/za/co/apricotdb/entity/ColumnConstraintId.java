package za.co.apricotdb.entity;

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
        return Objects.hash(columnId, columnId);
    }
}
