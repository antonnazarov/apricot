package za.co.apricotdb.viewport.entity;

import java.util.List;

import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * This interface is for a graphical representation of an entity.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public interface ApricotEntity extends ApricotElement {

    ApricotEntityShape getEntityShape();

    String getTableName();

    List<FieldDetail> getDetails();

    boolean isSlave();

    List<ApricotRelationship> getPrimaryLinks();

    List<ApricotRelationship> getForeignLinks();

    void addLink(ApricotRelationship link, boolean primary);
}
