package za.co.apricotdb.persistence.entity;

import java.io.Serializable;

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
import javax.persistence.Table;

/**
 * The apricot_object_layout table.
 * 
 * @author Anton Nazarov
 * @since 05/01/2018
 */
@Entity
@Table(name = "apricot_object_layout")
@NamedQuery(name = "ApricotObjectLayout.getLayoutsByType", query = "SELECT ol FROM ApricotObjectLayout ol WHERE ol.view = :view AND ol.objectType = :objectType")
@NamedQuery(name = "ApricotObjectLayout.getLayoutByName", query = "SELECT ol FROM ApricotObjectLayout ol WHERE ol.view = :view AND ol.objectName = :objectName")
@NamedQuery(name = "ApricotObjectLayout.getLayoutsForView", query = "SELECT ol FROM ApricotObjectLayout ol WHERE ol.view = :view")
@NamedQuery(name = "ApricotObjectLayout.getLayoutsForProject", query = "SELECT ol FROM ApricotProject p JOIN p.views v JOIN v.objectLayouts ol WHERE p = :project AND ol.objectName = :objectName")
@NamedQuery(name = "ApricotObjectLayout.getRelationshipLayoutsForProject", query = "SELECT ol FROM ApricotProject p JOIN p.views v JOIN v.objectLayouts ol WHERE p = :project AND ol.objectType = :objectType AND ol.objectName LIKE CONCAT('%', :objectName, '%')")
public class ApricotObjectLayout implements Serializable {

    private static final long serialVersionUID = -7584762504719191646L;

    public ApricotObjectLayout() {
    }

    public ApricotObjectLayout(LayoutObjectType objectType, String objectName, String objectLayout, ApricotView view) {
        this.objectType = objectType;
        this.objectName = objectName;
        this.objectLayout = objectLayout;
        this.view = view;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "layout_id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "object_type")
    private LayoutObjectType objectType;

    @Column(name = "object_name")
    private String objectName;

    @Column(name = "object_layout")
    private String objectLayout;

    @NoExport
    @ManyToOne
    @JoinColumn(name = "view_id")
    private ApricotView view;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LayoutObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(LayoutObjectType objectType) {
        this.objectType = objectType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectLayout() {
        return objectLayout;
    }

    public void setObjectLayout(String objectLayout) {
        this.objectLayout = objectLayout;
    }

    public ApricotView getView() {
        return view;
    }

    public void setView(ApricotView view) {
        this.view = view;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApricotObjectLayout: ").append("id=[").append(id).append("], ").append("objectName=[")
                .append(objectName).append("], ").append("objectLayout=[").append(objectLayout).append("]");

        return sb.toString();
    }

    public ApricotObjectLayout clone() {
        ApricotObjectLayout clone = new ApricotObjectLayout(this.objectType, this.objectName, this.objectLayout,
                this.view);

        return clone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
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
        ApricotObjectLayout other = (ApricotObjectLayout) obj;
        if (objectName == null) {
            if (other.objectName != null)
                return false;
        } else if (!objectName.equals(other.objectName))
            return false;
        return true;
    }
}
