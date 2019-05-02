package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

/**
 * This entity class represents the table apricot_view.
 * 
 * @author Anton Nazarov
 * @since 04/01/2019
 */
@Entity
@Table(name = "apricot_view")
@NamedQuery(name = "ApricotView.getGeneralView", query = "SELECT vw FROM ApricotView vw WHERE vw.project = :project AND vw.general = true")
@NamedQuery(name = "ApricotView.getAllViewsOrdered", query = "SELECT vw FROM ApricotView vw WHERE vw.project = :project ORDER by vw.ordinalPosition")
@NamedQuery(name = "ApricotView.getViewByName", query = "SELECT vw FROM ApricotView vw WHERE vw.project = :project AND vw.name = :name")
@NamedQuery(name = "ApricotView.getViewMaxOrdinalPosition", query = "SELECT MAX(vw.ordinalPosition) FROM ApricotView vw WHERE vw.project = :project")
@NamedQuery(name = "ApricotView.getViewsByObjectName", query = "SELECT vw FROM ApricotView vw JOIN vw.objectLayouts l WHERE vw.project = :project AND l.objectType = :objectType AND l.objectName = :objectName")
@NamedQuery(name = "ApricotView.getCurrentView", query = "SELECT vw FROM ApricotView vw WHERE vw.project = :project AND vw.current = :current")
public class ApricotView implements Serializable {

    public static final String MAIN_VIEW = "Main View";
    private static final long serialVersionUID = 8705614293066261879L;

    public ApricotView() {
    }

    public ApricotView(String name, String comment, java.util.Date created, java.util.Date updated, boolean general,
            int ordinalPosition, ApricotProject project, List<ApricotObjectLayout> objectLayouts, boolean current,
            ViewDetailLevel detailLevel) {
        this.name = name;
        this.comment = comment;
        this.created = created;
        this.updated = updated;
        this.general = general;
        this.ordinalPosition = ordinalPosition;
        this.project = project;
        this.objectLayouts = objectLayouts;
        this.current = current;
        this.detailLevel = detailLevel;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private long id;

    @Column(name = "view_name")
    private String name;

    @Column(name = "view_comment")
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "view_created")
    private java.util.Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "view_updated")
    private java.util.Date updated;

    @Column(name = "is_general")
    private boolean general;

    @Column(name = "ordinal_position")
    private int ordinalPosition;

    @Column(name = "is_current")
    private boolean current;

    @Enumerated(EnumType.STRING)
    @Column(name = "detail_level")
    private ViewDetailLevel detailLevel;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ApricotProject project;

    @OneToMany(mappedBy = "view", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApricotObjectLayout> objectLayouts = new ArrayList<>();

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public boolean isGeneral() {
        return general;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public ApricotProject getProject() {
        return project;
    }

    public void setProject(ApricotProject project) {
        this.project = project;
    }

    public List<ApricotObjectLayout> getObjectLayouts() {
        return objectLayouts;
    }

    public void setObjectLayouts(List<ApricotObjectLayout> objectLayouts) {
        this.objectLayouts = objectLayouts;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public ViewDetailLevel getDetailLevel() {
        return detailLevel;
    }

    public void setDetailLevel(ViewDetailLevel detailLevel) {
        this.detailLevel = detailLevel;
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApricotView other = (ApricotView) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
