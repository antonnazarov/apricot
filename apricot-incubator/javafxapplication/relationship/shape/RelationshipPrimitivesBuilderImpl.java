package javafxapplication.relationship.shape;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Implementation of the graphical primitives of the link.
 * 
 * @author Anton Nazarov
 * @since 23/11/2018
 */
public class RelationshipPrimitivesBuilderImpl implements RelationshipPrimitivesBuilder {
    
    public static final double LINK_END_DIAMETER = 6;
    public static final double OPTIONAL_START_LENGTH = 10;
    public static final double OPTIONAL_START_CORRECTION = 3;
   

    @Override
    public Shape getOptionalStart() {
        Rectangle r = new Rectangle(OPTIONAL_START_LENGTH, OPTIONAL_START_LENGTH);
        r.setRotate(45);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);

        return r;
    }

    @Override
    public Shape getRuler() {
        Rectangle r = new Rectangle(OPTIONAL_START_LENGTH, OPTIONAL_START_LENGTH);
        r.setFill(Color.BLACK);
        r.setStroke(Color.BLACK);
        
        r.managedProperty().bind(r.visibleProperty());
        r.setVisible(false);

        return r;
    }

    @Override
    public Shape getEnd(Point2D p) {
        Circle c = new Circle(LINK_END_DIAMETER);
        c.setCenterX(p.getX());
        c.setCenterY(p.getY());

        c.setFill(Color.BLACK);

        return c;
    }
}
