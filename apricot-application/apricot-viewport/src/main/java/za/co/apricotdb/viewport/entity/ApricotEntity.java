package za.co.apricotdb.viewport.entity;

import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

import java.util.List;

/**
 * This interface is for a graphical representation of an entity.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public interface ApricotEntity extends ApricotElement {

    ApricotEntityShape getEntityShape();

    String getTableName();
    
    void setTableName(String name);

    List<FieldDetail> getDetails();

    boolean isSlave();

    List<ApricotRelationship> getPrimaryLinks();

    List<ApricotRelationship> getForeignLinks();

    void addLink(ApricotRelationship link, boolean primary);
    
    boolean isParentAbsent();
    
    boolean isChildAbsent();

    void setSelectPrimaryRelationshipsFlag(boolean flag);
}
