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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="table_id")
    private long id;
    
    @Column(name="table_name") 
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
        StringBuilder sb = new StringBuilder("ApricotTable: ");
        sb.append("id=[").append(id).append("], ");
        sb.append("name=[").append(name).append("], ");
        sb.append("columns=[").append(columns).append("], ");
        sb.append("constraints=[").append(constraints).append("]\n");
        
        return sb.toString();
    }
}
