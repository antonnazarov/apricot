package javafxapplication.relationship.shape;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

/**
 * Builder of the graphical primitives used in the entity link representation.
 *
 * @author Anton Nazarov
 * @since 23/11/2018
 */
public interface RelationshipPrimitivesBuilder {
    
    Shape getOptionalStart();

    Shape getRuler();

    Shape getEnd(Point2D p);
}
