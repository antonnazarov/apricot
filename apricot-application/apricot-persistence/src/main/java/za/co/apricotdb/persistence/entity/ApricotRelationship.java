package za.co.apricotdb.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity for apricot_relationship.
 *
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name = "apricot_relationship")
@NamedQuery(name="ApricotRelationship.getRelationshipsForTables", query="SELECT rl FROM ApricotRelationship rl WHERE rl.child.table.name IN (:tables) OR rl.parent.table.name IN (:tables)")
public class ApricotRelationship implements Serializable {

    private static final long serialVersionUID = 2135283859031176938L;

    public ApricotRelationship() {}
    
    public ApricotRelationship(ApricotConstraint parent, ApricotConstraint child) {
        this.parent = parent;
        this.child = child;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "parent_constraint_id")
    private ApricotConstraint parent;

    @ManyToOne
    @JoinColumn(name = "child_constraint_id")
    private ApricotConstraint child;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApricotConstraint getParent() {
        return parent;
    }

    public void setParent(ApricotConstraint parent) {
        this.parent = parent;
    }

    public ApricotConstraint getChild() {
        return child;
    }

    public void setChild(ApricotConstraint child) {
        this.child = child;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nApricotRelationship: ");
        sb.append("parent table=[").append(parent.getTable().getName()).append("], ");
        sb.append("child table=[").append(child.getTable().getName()).append("]");

        return sb.toString();
    }
}
