package javafxapplication.canvas;

import javafxapplication.entity.ApricotEntity;
import javafxapplication.relationship.ApricotEntityLink;

/**
 * The canvas on which the ER- diagram will be drawn.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public interface ApricotEntityRelationshipCanvas {

    void addEntity(ApricotEntity entity);

    void addLink(ApricotEntityLink relationship);

    void orderElements();

    ApricotEntity findEntityByName(String name);

    void deleteElement(ApricotERElement element);
}
