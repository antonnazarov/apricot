package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import za.co.apricotdb.viewport.relationship.RelationshipType;

/**
 * The implementation of the IDEF1x specific builder of the relationship
 * primitives.
 * 
 * @author Anton Nazarov
 * @since 25/05/2019
 */
public class Idef1xPrimitivesBuilder extends PrimitivesBuilderImpl {
    public static final double LINK_END_DIAMETER = 6;
    public static final double OPTIONAL_START_LENGTH = 10;
    public static final double OPTIONAL_START_CORRECTION = 3;

    @Override
    public Group getStart(RelationshipType type) {
        Rectangle r = new Rectangle(OPTIONAL_START_LENGTH, OPTIONAL_START_LENGTH);
        r.setRotate(45);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);

        return new Group(r);
    }

    @Override
    public Group getEnd(RelationshipType type) {
        Circle c = new Circle(LINK_END_DIAMETER);
        c.setFill(Color.BLACK);

        return new Group(c);
    }

    @Override
    public void addStartElement(RelationshipType type, Point2D parentStart, Side parentSide,
            ApricotRelationshipShape shape) {
        if (type == RelationshipType.OPTIONAL_NON_IDENTIFYING) {
            Group startElement = getStart(type);

            switch (parentSide) {
            case RIGHT:
                startElement.setLayoutX(parentStart.getX() + 2);
                break;
            case LEFT:
                startElement.setLayoutX(parentStart.getX() - (OPTIONAL_START_LENGTH + 2));
                break;
            case TOP:
                break;
            default:
                break;
            }
            startElement.setLayoutY(parentStart.getY() - OPTIONAL_START_LENGTH / 2);
            shape.setStartElement(startElement);
        }
    }

    @Override
    public void addEndElement(RelationshipType type, Point2D childEnd, Side childSide, ApricotRelationshipShape shape) {
        Group endElement = getEnd(type);
        switch (childSide) {
        case RIGHT:
            endElement.setLayoutX(childEnd.getX() + LINK_END_DIAMETER);
            endElement.setLayoutY(childEnd.getY());
            break;
        case LEFT:
            endElement.setLayoutX(childEnd.getX() - LINK_END_DIAMETER);
            endElement.setLayoutY(childEnd.getY());
            break;
        case TOP:
            endElement.setLayoutX(childEnd.getX());
            endElement.setLayoutY(childEnd.getY() - LINK_END_DIAMETER);
            break;
        default:
            break;
        }
        shape.setEndElement(endElement);
    }
}
