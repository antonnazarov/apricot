package za.co.apricotdb.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

/**
 * This entity class represents the table apricot_view. 
 * 
 * @author Anton Nazarov
 * @since 04/01/2019
 */
@Entity
@Table(name = "apricot_view")
@NamedQuery(name="ApricotView.getGeneralView", query="SELECT vw FROM ApricotView vw WHERE vw.project = :project AND vw.general = true")
public class ApricotView implements Serializable {

    private static final long serialVersionUID = 8705614293066261879L;
    
    public ApricotView() {}
    
    public ApricotView(String name, String comment, java.util.Date created,
            java.util.Date updated, boolean general, int ordinalPosition, ApricotProject project,
            List<ApricotObjectLayout> objectLayouts) {
        this.name = name;
        this.comment = comment;
        this.created = created;
        this.updated = updated;
        this.general = general;
        this.ordinalPosition = ordinalPosition;
        this.project = project;
        this.objectLayouts = objectLayouts;
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
}
