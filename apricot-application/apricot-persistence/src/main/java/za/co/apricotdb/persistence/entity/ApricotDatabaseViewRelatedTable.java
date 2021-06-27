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
 * This Entity represents the APRICOT_DBVIEW_RELATED_TABLE table.
 *
 * @author Anton Nazarov
 * @since 23/06/2021
 */
@Entity
@Table(name = "APRICOT_DBVIEW_RELATED_TABLE")
public class ApricotDatabaseViewRelatedTable implements Serializable {

    private static final long serialVersionUID = -3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REL_TABLE_ID")
    private long id;

    @Column(name = "REL_TABLE_NAME")
    private String refTableName;

    @Column(name = "REL_TABLE_COLUMNS")
    private String refTableColumns;

    @NoExport
    @ManyToOne
    @JoinColumn(name = "DB_VIEW_ID")
    private ApricotDatabaseView databaseView;

    public ApricotDatabaseViewRelatedTable() {}

    public ApricotDatabaseViewRelatedTable(String refTableName, String refTableColumns) {
        this.refTableName = refTableName;
        this.refTableColumns = refTableColumns;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRefTableName() {
        return refTableName;
    }

    public void setRefTableName(String refTableName) {
        this.refTableName = refTableName;
    }

    public String getRefTableColumns() {
        return refTableColumns;
    }

    public void setRefTableColumns(String refTableColumns) {
        this.refTableColumns = refTableColumns;
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
        ApricotDatabaseViewRelatedTable that = (ApricotDatabaseViewRelatedTable) o;
        return refTableName.equals(that.refTableName) && databaseView.equals(that.databaseView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refTableName, databaseView);
    }

    @Override
    public String toString() {
        return "ApricotDatabaseViewRelatedTable{" +
                "id=" + id +
                ", refTableName='" + refTableName + '\'' +
                ", refTableColumns='" + refTableColumns + '\'' +
                ", databaseView=" + databaseView.getDbViewName() +
                '}';
    }
}
