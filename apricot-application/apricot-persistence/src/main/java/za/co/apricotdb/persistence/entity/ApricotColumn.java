package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity for apricot_column table.
 *
 * @author Anton Nazarov
 * @since 22/09/2018
 */
@Entity
@Table(name = "apricot_column")
public class ApricotColumn implements Serializable {

    private static final long serialVersionUID = 7415678971662772902L;

    public ApricotColumn() {
    }

    public ApricotColumn(String name, int ordinalPosition, boolean nullable, String dataType, String valueLength,
            ApricotTable table) {
        this.name = name;
        this.ordinalPosition = ordinalPosition;
        this.nullable = nullable;
        this.dataType = dataType;
        this.valueLength = valueLength;
        this.table = table;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_id")
    private long id;
    
    @NoExport
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApricotColumn column = (ApricotColumn) o;

        return name.equalsIgnoreCase(column.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\ncolumn: ");
        sb.append("id=[").append(id).append("], ");
        sb.append("name=[").append(name).append("], ");
        sb.append("ordinalPosition=[").append(ordinalPosition).append("], ");
        sb.append("nullable=[").append(nullable).append("], ");
        sb.append("dataType=[").append(dataType).append("], ");
        sb.append("valueLength=[").append(valueLength).append("], ");
        sb.append("table=[").append(table.getName()).append("]");

        return sb.toString();
    }
}
