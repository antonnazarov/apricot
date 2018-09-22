package za.co.apricotdb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity for apricot_relationship.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name = "apricot_constraint")
public class ApricotRelationship implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="relationship_id")
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
}


