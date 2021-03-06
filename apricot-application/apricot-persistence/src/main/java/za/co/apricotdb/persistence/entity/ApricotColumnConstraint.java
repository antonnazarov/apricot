package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity for the associative table apricot_column_in_constraint.
 *
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name = "apricot_column_in_constraint")
@NamedQuery(name = "ApricotColumnConstraint.getColumnsByConstraint", query="SELECT acc FROM ApricotColumnConstraint acc WHERE acc.constraint = :constraint")
@NamedQuery(name = "ApricotColumnConstraint.getColumnConstraintByProject", query="SELECT DISTINCT acc FROM ApricotColumnConstraint acc WHERE acc.column.table.snapshot.project = :project")
public class ApricotColumnConstraint implements Serializable {

    private static final long serialVersionUID = -4404654225693095401L;

    @EmbeddedId
    private ColumnConstraintId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("constraintId")
    @JoinColumn(name = "constraint_id")
    private ApricotConstraint constraint;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("columnId")
    @JoinColumn(name = "column_id")
    private ApricotColumn column;
    
    @Column(name = "ordinal_position")
    private int ordinalPosition;

    private ApricotColumnConstraint() {
    }

    public ApricotColumnConstraint(ApricotConstraint constraint, ApricotColumn column) {
        this.constraint = constraint;
        this.column = column;
        this.id = new ColumnConstraintId(constraint.getId(), column.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApricotColumnConstraint that = (ApricotColumnConstraint) o;
        return Objects.equals(constraint, that.constraint)
                && Objects.equals(column, that.column);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraint, column);
    }

    public ColumnConstraintId getId() {
        return id;
    }

    public void setId(ColumnConstraintId id) {
        this.id = id;
    }

    public ApricotConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(ApricotConstraint constraint) {
        this.constraint = constraint;
    }

    public ApricotColumn getColumn() {
        return column;
    }

    public void setColumn(ApricotColumn column) {
        this.column = column;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }
    
    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder("\n* ");
       sb.append("column=[").append(column.getName()).append("], ");
       sb.append("ordinalPosition=[").append(ordinalPosition).append("]");
       
       return sb.toString();
    }    
}
