package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * the entity class for the table apricot_project.
 * 
 * @author Anton Nazarov
 * @since 04/01/2019
 */
@Entity
@Table(name = "apricot_project")
@NamedQuery(name = "ApricotProject.getAllProjects", query = "SELECT p FROM ApricotProject p WHERE p.name <> :undo_project ORDER BY p.created DESC")
@NamedQuery(name = "ApricotProject.getProjectByName", query = "SELECT p FROM ApricotProject p WHERE p.name = :name")
public class ApricotProject implements Serializable {

    private static final long serialVersionUID = -8732917531930595931L;

    public ApricotProject() {
    }

    public ApricotProject(String name, String description, String targetDatabase, boolean current,
            java.util.Date created, List<ApricotSnapshot> snapshots, List<ApricotProjectParameter> parameters,
            List<ApricotView> views, ERDNotation erdNotation) {
        this.name = name;
        this.description = description;
        this.targetDatabase = targetDatabase;
        this.current = current;
        this.created = created;
        this.snapshots = snapshots;
        this.parameters = parameters;
        this.views = views;
        this.erdNotation = erdNotation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long id;

    @Column(name = "project_name")
    private String name;

    @Column(name = "project_description")
    private String description;

    @Column(name = "target_database")
    private String targetDatabase;

    @Column(name = "is_current")
    private boolean current;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "project_created")
    private java.util.Date created;

    @Enumerated(EnumType.STRING)
    @Column(name = "erd_notation")
    private ERDNotation erdNotation;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotSnapshot> snapshots = new ArrayList<>();

    @NoExport
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotProjectParameter> parameters = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotView> views = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetDatabase() {
        return targetDatabase;
    }

    public void setTargetDatabase(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    public List<ApricotSnapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<ApricotSnapshot> snapshots) {
        this.snapshots = snapshots;
    }

    public List<ApricotProjectParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<ApricotProjectParameter> parameters) {
        this.parameters = parameters;
    }

    public List<ApricotView> getViews() {
        return views;
    }

    public void setViews(List<ApricotView> views) {
        this.views = views;
    }

    public String getFormattedCreatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(created);
    }

    public ERDNotation getErdNotation() {
        return erdNotation;
    }

    public void setErdNotation(ERDNotation erdNotation) {
        this.erdNotation = erdNotation;
    }

    @Override
    public String toString() {
        return "ApricotProject{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApricotProject that = (ApricotProject) o;
        return id == that.id &&
                current == that.current &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
