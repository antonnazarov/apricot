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
@NamedQuery(name = "ApricotRelationship.getRelationshipsForTables", query = "SELECT DISTINCT rl FROM ApricotRelationship rl WHERE rl.child.table.name IN (:tables) AND rl.parent.table.name IN (:tables)")
@NamedQuery(name = "ApricotRelationship.getRelationshipsForTableList", query = "SELECT DISTINCT rl FROM ApricotRelationship rl WHERE rl.child.table IN (:tables) AND rl.parent.table IN (:tables)")
@NamedQuery(name = "ApricotRelationship.getRelationshipsForTable", query = "SELECT DISTINCT rl FROM ApricotRelationship rl WHERE rl.child.table = :table OR rl.parent.table = :table")
@NamedQuery(name = "ApricotRelationship.findRelationshipsByParentConstraint", query = "SELECT rl FROM ApricotRelationship rl WHERE rl.parent = :parentConstraint")
@NamedQuery(name = "ApricotRelationship.findRelationshipsByConstraint", query = "SELECT rl FROM ApricotRelationship rl WHERE rl.parent = :constraint OR rl.child = :constraint")
@NamedQuery(name = "ApricotRelationship.findRelationshipsBySnapshot", query = "SELECT rl FROM ApricotRelationship rl WHERE rl.parent.table.snapshot = :snapshot")
@NamedQuery(name = "ApricotRelationship.findRelationshipsByProject", query = "SELECT rl FROM ApricotRelationship rl WHERE rl.parent.table.snapshot.project = :project")
public class ApricotRelationship implements Serializable {

    private static final long serialVersionUID = 2135283859031176938L;

    public ApricotRelationship() {
    }

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
        StringBuilder sb = new StringBuilder("ApricotRelationship: ");
        sb.append("parent table=[").append(parent.getTable().getName()).append("], ");
        sb.append("child table=[").append(child.getTable().getName()).append("]");

        return sb.toString();
    }

    @Override
    public boolean equals(Object rel) {
        if (rel instanceof ApricotRelationship) {
            return ((ApricotRelationship) rel).getName().equals(this.getName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
    
    /**
     * The "logical" name of the constraint composed as 
     * <parent_table>|<child_table>|<primary_key_field>|<foreign_key_field>
     */
    public String getName() {
        StringBuilder sb = new StringBuilder();

        //  checking if the database is consistent
        if (parent.getColumns() == null || parent.getColumns().size() == 0 || child.getColumns() == null || child.getColumns().size() == 0) {
            return "undefined";
        }

        sb.append(parent.getTable().getName()).append("|")
            .append(child.getTable().getName()).append("|")
            .append(parent.getColumns().get(0).getColumn().getName()).append("|")
            .append(child.getColumns().get(0).getColumn().getName()).append("|");
        
        return sb.toString();
    }
}
