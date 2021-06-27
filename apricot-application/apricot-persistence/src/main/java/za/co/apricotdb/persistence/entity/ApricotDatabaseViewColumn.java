package za.co.apricotdb.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * This Entity represents the APRICOT_DBVIEW table.
 *
 * @author Anton Nazarov
 * @since 23/06/2021
 */
@Entity
@Table(name = "APRICOT_DBVIEW_COLUMN")
public class ApricotDatabaseViewColumn implements Serializable {

    private static final long serialVersionUID = -2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DBVIEW_COLUMN_ID")
    private long id;

    @Column(name = "DBVIEW_COLUMN_NAME")
    private String name;

    @Column(name = "DBVIEW_COLUMN_ORDINAL_POSITION")
    private int ordinalPosition;

    @Column(name = "DBVIEW_COLUMN_IS_NULLABLE")
    private boolean nullable;

    @Column(name = "DBVIEW_COLUMN_DATA_TYPE")
    private String dataType;

    @Column(name = "DBVIEW_COLUMN_VALUE_LENGTH")
    private String valueLength;

    @NoExport
    @ManyToOne
    @JoinColumn(name = "DB_VIEW_ID")
    private ApricotDatabaseView databaseView;

    public ApricotDatabaseViewColumn() {}

    public ApricotDatabaseViewColumn(String name, int ordinalPosition, boolean nullable, String dataType, String valueLength) {
        this.name = name;
        this.ordinalPosition = ordinalPosition;
        this.nullable = nullable;
        this.dataType = dataType;
        this.valueLength = valueLength;
    }

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

    public ApricotDatabaseView getDatabaseView() {
        return databaseView;
    }

    public void setDatabaseView(ApricotDatabaseView databaseView) {
        this.databaseView = databaseView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApricotDatabaseViewColumn that = (ApricotDatabaseViewColumn) o;
        return name.equals(that.name) && databaseView.equals(that.databaseView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, databaseView);
    }

    @Override
    public String toString() {
        return "ApricotDatabaseViewColumn{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ordinalPosition=" + ordinalPosition +
                ", nullable=" + nullable +
                ", dataType='" + dataType + '\'' +
                ", valueLength='" + valueLength + '\'' +
                ", databaseView=" + databaseView.getDbViewName() +
                '}';
    }
}
