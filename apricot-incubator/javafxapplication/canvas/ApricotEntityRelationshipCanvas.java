package javafxapplication.canvas;

import javafxapplication.entity.ApricotEntity;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.relationship.shape.ApricotLinkShape;

/**
 * The canvas on which the ER- diagram will be drawn.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ApricotEntityRelationshipCanvas {

    void addEntity(ApricotEntityShape entity);

    void addLink(ApricotLinkShape relationship);

    void orderElements();

    ApricotEntity findEntityByName(String name);

    void deleteElement(ApricotERElement element);
}
