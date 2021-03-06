package za.co.apricotdb.persistence.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity for the apricot_snapshot table.
 * 
 * @author Anton Nazarov
 * @since 04/01/2019
 */
@Entity
@Table(name = "apricot_snapshot")
@NamedQuery(name = "ApricotSnapshot.getDefaultSnapshot", query = "SELECT sn FROM ApricotSnapshot sn WHERE sn.project = :project AND sn.defaultSnapshot = true")
@NamedQuery(name = "ApricotSnapshot.getAllSnapshotsInProject", query = "SELECT sn FROM ApricotSnapshot sn WHERE sn.project = :project ORDER by sn.created")
@NamedQuery(name = "ApricotSnapshot.getSnapshotByName", query = "SELECT sn FROM ApricotSnapshot sn WHERE sn.name = :name AND sn.project = :project")
@NamedQuery(name = "ApricotSnapshot.getSnapshotsByProjectName", query = "SELECT sn FROM ApricotSnapshot sn WHERE sn.project.name = :projectName")
public class ApricotSnapshot implements Serializable {

    public final static int SNAPSHOT_COMMENT_LENGTH = 500;
    private static final long serialVersionUID = -8750109610659026590L;

    public ApricotSnapshot() {
    }

    public ApricotSnapshot(String name, java.util.Date created, java.util.Date updated, String comment,
            boolean defaultSnapshot, ApricotProject project, List<ApricotTable> tables) {
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.comment = comment;
        this.defaultSnapshot = defaultSnapshot;
        this.project = project;
        this.tables = tables;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private long id;

    @Column(name = "snapshot_name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "snapshot_created")
    private java.util.Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "snapshot_updated")
    private java.util.Date updated;

    @Column(name = "snapshot_comment")
    private String comment;

    @Column(name = "is_default")
    private boolean defaultSnapshot;

    @NoExport
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ApricotProject project;

    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotTable> tables = new ArrayList<>();

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

    public java.util.Date getCreated() {
        return created;
    }

    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public void setUpdated(java.util.Date updated) {
        this.updated = updated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isDefaultSnapshot() {
        return defaultSnapshot;
    }

    public void setDefaultSnapshot(boolean defaultSnapshot) {
        this.defaultSnapshot = defaultSnapshot;
    }

    public ApricotProject getProject() {
        return project;
    }

    public void setProject(ApricotProject project) {
        this.project = project;
    }

    public List<ApricotTable> getTables() {
        return tables;
    }

    public void setTables(List<ApricotTable> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<ApricotTable> sorted = new ArrayList<>(tables);
        sorted.sort((t1, t2) -> {
            return t1.getName().compareTo(t2.getName());
        });
        sb.append("snapshot: ").append(name).append(" (").append(id).append(")\n");
        for (ApricotTable t : sorted) {
            sb.append(t.getName()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApricotSnapshot other = (ApricotSnapshot) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public List<String> getTablesAsString() {
        List<String> ret = new ArrayList<>();

        for (ApricotTable table : tables) {
            ret.add(table.getName());
        }

        return ret;
    }
}
