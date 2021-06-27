package za.co.apricotdb.persistence.entity;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * This Entity represents the APRICOT_DBVIEW table.
 *
 * @author Anton Nazarov
 * @since 23/06/2021
 */
@Entity
@Table(name = "APRICOT_DBVIEW")
public class ApricotDatabaseView implements Serializable {

    private static final long serialVersionUID = 7415678979875102902L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DB_VIEW_ID")
    private long id;

    @Column(name = "DB_VIEW_NAME")
    private String dbViewName;

    @Column(name = "DB_VIEW_DEFINITION")
    private String dbViewDefinition;

    @NoExport
    @ManyToOne
    @JoinColumn(name = "SNAPSHOT_ID")
    private ApricotSnapshot snapshot;

    @OneToMany(mappedBy = "databaseView", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotDatabaseViewColumn> viewColumns = new ArrayList<>();

    @OneToMany(mappedBy = "databaseView", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotDatabaseViewRelatedTable> relatedTables = new ArrayList<>();

    public ApricotDatabaseView() {}

    public ApricotDatabaseView(String dbViewName, String dbViewDefinition, ApricotSnapshot snapshot,
                               List<ApricotDatabaseViewColumn> viewColumns,
                               List<ApricotDatabaseViewRelatedTable> relatedTables) {
        this.dbViewName = dbViewName;
        this.dbViewDefinition = dbViewDefinition;
        this.snapshot = snapshot;
        this.viewColumns = viewColumns;
        this.relatedTables = relatedTables;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDbViewName() {
        return dbViewName;
    }

    public void setDbViewName(String dbViewName) {
        this.dbViewName = dbViewName;
    }

    public String getDbViewDefinition() {
        return dbViewDefinition;
    }

    public void setDbViewDefinition(String dbViewDefinition) {
        this.dbViewDefinition = dbViewDefinition;
    }

    public ApricotSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(ApricotSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public List<ApricotDatabaseViewColumn> getViewColumns() {
        viewColumns.sort(Comparator.comparingInt(ApricotDatabaseViewColumn::getOrdinalPosition));

        return viewColumns;
    }

    public void setViewColumns(List<ApricotDatabaseViewColumn> columns) {
        this.viewColumns = columns;
    }

    public List<ApricotDatabaseViewRelatedTable> getRelatedTables() {
        return relatedTables;
    }

    public void setRelatedTables(List<ApricotDatabaseViewRelatedTable> relatedTables) {
        this.relatedTables = relatedTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApricotDatabaseView that = (ApricotDatabaseView) o;
        return dbViewName.equals(that.dbViewName) && snapshot.equals(that.snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbViewName, snapshot);
    }

    @Override
    public String toString() {
        return "ApricotDatabaseView{" +
                "id=" + id +
                ", dbViewName='" + dbViewName + '\'' +
                ", dbViewDefinition='" + dbViewDefinition + '\'' +
                ", snapshot=" + snapshot +
                ", viewColumns=" + viewColumns +
                ", relatedTables=" + relatedTables +
                '}';
    }
}
