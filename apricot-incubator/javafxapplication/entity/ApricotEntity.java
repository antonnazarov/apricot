package javafxapplication.entity;

import java.util.List;

import javafxapplication.canvas.ApricotElement;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.relationship.ApricotEntityLink;

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

    List<ApricotEntityLink> getPrimaryLinks();

    List<ApricotEntityLink> getForeignLinks();

    void addLink(ApricotEntityLink link, boolean primary);
}
