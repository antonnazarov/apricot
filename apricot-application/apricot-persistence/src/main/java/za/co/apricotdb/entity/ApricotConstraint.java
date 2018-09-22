package za.co.apricotdb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity for apricot_constraint table.
 *
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name = "apricot_constraint")
public class ApricotConstraint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "constraint_id")
    private long id;

    @Column(name = "constraint_name")
    private String name;

    @Column(name = "constraint_type")
    @Enumerated(EnumType.STRING)
    private ConstraintType type;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private ApricotTable table;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotRelationship> relationships = new ArrayList<>();

    @OneToMany(mappedBy = "constraint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotColumnConstraint> columns = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstraintType getType() {
        return type;
    }

    public void setType(ConstraintType type) {
        this.type = type;
    }

    public ApricotTable getTable() {
        return table;
    }

    public void setTable(ApricotTable table) {
        this.table = table;
    }

    public List<ApricotRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<ApricotRelationship> relationships) {
        this.relationships = relationships;
    }

    public List<ApricotColumnConstraint> getColumns() {
        return columns;
    }

    public void setColumns(List<ApricotColumnConstraint> columns) {
        this.columns = columns;
    }

    public void addColumn(ApricotColumn column) {
        ApricotColumnConstraint columnConstraint = new ApricotColumnConstraint(this, column);
        columns.add(columnConstraint);
        column.getConstraints().add(columnConstraint);
    }

    public void removeColumn(ApricotColumn column) {
        for (Iterator<ApricotColumnConstraint> iterator = columns.iterator(); iterator.hasNext(); ) {
            ApricotColumnConstraint columnConstraint = iterator.next();

            if (columnConstraint.getConstraint().equals(this)
                    && columnConstraint.getColumn().equals(column)) {
                iterator.remove();
                columnConstraint.getColumn().getConstraints().remove(columnConstraint);
                columnConstraint.setConstraint(null);
                columnConstraint.setColumn(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApricotConstraint constraint = (ApricotConstraint) o;
        return Objects.equals(name, constraint.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
