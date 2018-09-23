package za.co.apricotdb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Entity for apricot_column table.
 * 
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name="apricot_column")
public class ApricotColumn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_id")
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "table_id")
    private ApricotTable table;
    
    @Column(name = "column_name")
    private String name;
    
    @Column(name = "ordinal_position")
    private int ordinalPosition;
    
    @Column(name = "is_nullable")
    private boolean nullable;
    
    @Column(name = "data_type")
    private String dataType;
    
    @Column(name = "value_length")
    private String valueLength;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotColumnConstraint> constraints = new ArrayList<>();
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApricotTable getTable() {
        return table;
    }

    public void setTable(ApricotTable table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValueLength() {
        return valueLength;
    }

    public void setValueLength(String valueLength) {
        this.valueLength = valueLength;
    }

    public List<ApricotColumnConstraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<ApricotColumnConstraint> constraints) {
        this.constraints = constraints;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass()) 
            return false;
 
        ApricotColumn column = (ApricotColumn) o;
        return Objects.equals(name, column.name);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder("column: ");
       sb.append("id=[").append(id).append("], ");
       sb.append("name=[").append(name).append("], ");
       sb.append("ordinalPosition=[").append(ordinalPosition).append("], ");
       sb.append("nullable=[").append(nullable).append("], ");
       sb.append("dataType=[").append(dataType).append("], ");
       sb.append("valueLength=[").append(valueLength).append("], ");
       sb.append("table=[").append(table.getName()).append("]\n");
       
       return sb.toString();
    }
 }

