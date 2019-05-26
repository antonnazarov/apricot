package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.relationship.RelationshipType;

/**
 * The implementation of the Crows Foot specific builder of the relationship
 * primitives.
 * 
 * @author Anton Nazarov
 * @since 25/05/2019
 */
public class CrowsFootPrimitivesBuilder extends PrimitivesBuilderImpl {

    public static final double DASH_LENGTH = 10;
    public static final double DASH_DISTANCE = 4;

    @Override
    public Group getStart(RelationshipType type) {
        Group g = new Group();
        Path p = null;
        switch (type) {
        case IDENTIFYING:
        case MANDATORY_NON_IDENTIFYING:
            p = new Path();
            p.getElements().add(new MoveTo(0, 0));
            p.getElements().add(new VLineTo(DASH_LENGTH));
            p.getElements().add(new MoveTo(DASH_DISTANCE, 0));
            p.getElements().add(new VLineTo(DASH_LENGTH));
            g.getChildren().add(p);
            break;
        case OPTIONAL_NON_IDENTIFYING:
            p = new Path();
            p.getElements().add(new MoveTo(0, 0));
            p.getElements().add(new VLineTo(DASH_LENGTH));
            g.getChildren().add(p);

            Circle c = new Circle(DASH_LENGTH / 2 - 1);
            c.setStroke(Color.BLACK);
            c.setFill(Color.WHITE);
            g.getChildren().add(c);
            c.setLayoutX(DASH_DISTANCE * 2);
            c.setLayoutY(DASH_LENGTH / 2);

            break;
        }

        return g;
    }

    @Override
    public Group getEnd(RelationshipType type) {
        Group g = new Group();

        double radius = DASH_LENGTH / 2 - 1;

        Circle c = new Circle(radius);
        c.setStroke(Color.BLACK);
        c.setFill(Color.WHITE);
        g.getChildren().add(c);

        Path p = new Path();
        p.getElements().add(new MoveTo(0, 0));
        p.getElements().add(new LineTo(DASH_LENGTH, -radius));
        p.getElements().add(new MoveTo(0, 0));
        p.getElements().add(new LineTo(DASH_LENGTH, radius));
        g.getChildren().add(p);
        p.setLayoutX(radius);

        return g;
    }

    @Override
    public void addStartElement(RelationshipType type, Point2D parentStart, Side parentSide,
            ApricotRelationshipShape shape) {
        Group startElement = getStart(type);

        switch (parentSide) {
        case RIGHT:
            startElement.setLayoutX(parentStart.getX() + 5);
            break;
        case LEFT:
            startElement.setRotate(180);
            if (type == RelationshipType.OPTIONAL_NON_IDENTIFYING) {
                startElement.setLayoutX(parentStart.getX() - (DASH_DISTANCE + 10));
            } else {
                startElement.setLayoutX(parentStart.getX() - (DASH_DISTANCE + 4));
            }
            break;
        case TOP:
            break;
        default:
            break;
        }
        startElement.setLayoutY(parentStart.getY() - DASH_DISTANCE / 2 - 3);
        shape.setStartElement(startElement);
    }

    @Override
    public void addEndElement(RelationshipType type, Point2D childEnd, Side childSide, ApricotRelationshipShape shape) {
        Group endElement = getEnd(type);
        switch (childSide) {
        case RIGHT:
            endElement.setRotate(180);
            endElement.setLayoutX(childEnd.getX() + DASH_LENGTH/2);
            endElement.setLayoutY(childEnd.getY());
            break;
        case LEFT:
            endElement.setLayoutX(childEnd.getX() - 15);
            endElement.setLayoutY(childEnd.getY());
            break;
        case TOP:
            break;
        default:
            break;
        }
        shape.setEndElement(endElement);
    }
}
