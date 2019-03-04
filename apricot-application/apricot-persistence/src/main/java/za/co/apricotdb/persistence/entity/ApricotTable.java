package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA Entity represents Apricot Table.
 *
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name = "apricot_table")
@NamedQuery(name="ApricotTable.getTablesByName", query="SELECT at FROM ApricotTable at WHERE at.name IN (:tables) AND at.snapshot = :snapshot")
@NamedQuery(name="ApricotTable.getTablesBySnapshot", query="SELECT at FROM ApricotTable at WHERE at.snapshot = :snapshot ORDER BY LOWER(at.name)")
@NamedQuery(name="ApricotTable.getTableByName", query="SELECT at FROM ApricotTable at WHERE at.name = :name AND at.snapshot = :snapshot")
public class ApricotTable implements Serializable {

    private static final long serialVersionUID = 7279471522618380758L;

    public ApricotTable() {
    }

    public ApricotTable(String name, List<ApricotColumn> columns, 
            List<ApricotConstraint> constraints, ApricotSnapshot snapshot) {
        this.name = name;
        this.columns = columns;
        this.constraints = constraints;
        this.snapshot = snapshot;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private long id;

    @Column(name = "table_name")
    private String name;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotColumn> columns = new ArrayList<>();

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotConstraint> constraints = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "snapshot_id")
    private ApricotSnapshot snapshot;

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

    public List<ApricotColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ApricotColumn> columns) {
        this.columns = columns;
    }

    public List<ApricotConstraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<ApricotConstraint> constraints) {
        this.constraints = constraints;
    }

    public ApricotSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(ApricotSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nApricotTable: ");
        sb.append("id=[").append(id).append("], ");
        sb.append("name=[").append(name).append("], ");
        sb.append("\ncolumns=[").append(columns).append("], ");
        sb.append("\nconstraints=[").append(constraints).append("]\n");
        sb.append("\nproject=[").append(snapshot.getName()).append("]\n");

        return sb.toString();
    }
    
    /**
     * Get column by its name.
     */
    public ApricotColumn getColumnByName(String columnName) {
        for (ApricotColumn c : columns) {
            if (c.getName().equals(columnName)) {
                return c;
            }
        }
        
        return null;
    }

    public ApricotColumn getColumnById(long id) {
        for (ApricotColumn c : columns) {
            if (c.getId() == id) {
                return c;
            }
        }
        
        return null;
    }

    /**
     * Get a list of columns by their names.
     */
    public List<ApricotColumn> getColumnListByNames(String names) {
        List<ApricotColumn> columns = new ArrayList<>();
        String[] nmArr = names.split(";");
        for (String name : nmArr) {
            ApricotColumn c = getColumnByName(name);
            if (name != null) {
                columns.add(c);
            }
        }
        
        return columns;
    }
    
    public ApricotRelationship establishChildConstraint(String parentConstraintName, ApricotTable childTable, String childConstraintName) {
        ApricotConstraint parent = getConstraintByName(parentConstraintName);
        ApricotConstraint child = childTable.getConstraintByName(childConstraintName);
        
        ApricotRelationship r = new ApricotRelationship(parent, child);
        
        return r;
    }
    
    public ApricotConstraint getConstraintByName(String name) {
        for (ApricotConstraint c : constraints) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        
        return null;
    }
    
    public ApricotConstraint getConstraintById(long id) {
        for (ApricotConstraint c : constraints) {
            if (c.getId() == id) {
                return c;
            }
        }
        
        return null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ApricotTable) {
            ApricotTable t = (ApricotTable) o;
            return this.name.equals(t.name);
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
