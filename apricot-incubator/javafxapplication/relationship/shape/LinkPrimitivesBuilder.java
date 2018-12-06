package javafxapplication.relationship.shape;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

/**
 * Builder of the graphical primitives used in the entity link representation.
 *
 * @author Anton Nazarov
 * @since 23/11/2018
 */
public interface LinkPrimitivesBuilder {
    static final double LINK_END_DIAMETER = 6;
    static final double OPTIONAL_START_LENGTH = 10;
    static final double OPTIONAL_START_CORRECTION = 3;

    Shape getOptionalStart();

    Shape getRuler();

    Shape getEnd(Point2D p);
}