package za.co.apricotdb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class ApricotTable implements Serializable {

    public ApricotTable() {
    }

    public ApricotTable(String name, List<ApricotColumn> columns, List<ApricotConstraint> constraints) {
        this.name = name;
        this.columns = columns;
        this.constraints = constraints;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nApricotTable: ");
        sb.append("id=[").append(id).append("], ");
        sb.append("name=[").append(name).append("], ");
        sb.append("\ncolumns=[").append(columns).append("], ");
        sb.append("\nconstraints=[").append(constraints).append("]\n");

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
        ApricotConstraint parent = getConstraingByName(parentConstraintName);
        ApricotConstraint child = childTable.getConstraingByName(childConstraintName);
        
        ApricotRelationship r = new ApricotRelationship(parent, child);
        
        return r;
    }
    
    public ApricotConstraint getConstraingByName(String name) {
        for (ApricotConstraint c : constraints) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        
        return null;
    }
}
