package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;

public class DirectShapeBuilder extends RelationshipShapeBuilderImpl {

    public DirectShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder,
            RelationshipTopology relationshipTopology) {
        super(primitivesBuilder, relationshipTopology);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        Side parentSide = relationshipTopology.getRelationshipSide(relationship, true);
        Side childSide = relationshipTopology.getRelationshipSide(relationship, false);
        Point2D parentStart = getParentStart(relationship, parentSide);
        Point2D childEnd = getChildEnd(relationship, childSide);

        DirectRelationship shape = new DirectRelationship(relationship);
        double defaultRulerX = getDefaultRulerX(parentStart, childEnd,
                TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild()));
        shape.setRulerX(defaultRulerX);

        addElements(relationship, parentStart, childEnd, parentSide, childSide, defaultRulerX, shape);

        return shape;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        if (relationship.getShape() != null) {
            Side parentSide = relationshipTopology.getRelationshipSide(relationship, true);
            Side childSide = relationshipTopology.getRelationshipSide(relationship, false);
            Point2D parentStart = getParentStart(relationship, parentSide);
            Point2D childEnd = getChildEnd(relationship, childSide);

            if (relationship.getShape() instanceof DirectRelationship) {
                DirectRelationship shape = (DirectRelationship) relationship.getShape();
                double correctedRulerX = correctRulerX(parentStart, childEnd, shape.getRulerX());
                shape.setRulerX(correctedRulerX);

                addElements(relationship, parentStart, childEnd, parentSide, childSide, correctedRulerX, shape);
            }
        }
    }

    private void addElements(ApricotRelationship relationship, Point2D parentStart, Point2D childEnd, Side parentSide,
            Side childSide, double rulerX, DirectRelationship shape) {
        addPath(parentStart, childEnd, rulerX, relationship.getRelationshipType(), shape);
        addStartElement(relationship.getRelationshipType(), parentStart, parentSide, shape);
        addEndElement(childEnd, childSide, shape);
        addRuler(parentStart, childEnd, rulerX, shape);
    }

    private void addRuler(Point2D parentStart, Point2D childEnd, double rulerX, DirectRelationship shape) {

        Shape ruler = primitivesBuilder.getRuler();

        ruler.setLayoutX(rulerX - RelationshipPrimitivesBuilderImpl.RULER_LENGTH / 2);
        double midY = Math.min(parentStart.getY(), childEnd.getY()) + Math.abs(parentStart.getY() - childEnd.getY()) / 2
                - RelationshipPrimitivesBuilderImpl.RULER_LENGTH / 2;
        ruler.setLayoutY(midY);

        shape.setRuler(ruler);
    }

    private double getDefaultRulerX(Point2D parentStart, Point2D childEnd, boolean isParentLeft) {
        double ret = 0;

        if (isParentLeft) {
            ret = parentStart.getX() + Math.abs(parentStart.getX() - childEnd.getX()) / 2;
        } else {
            ret = childEnd.getX() + Math.abs(parentStart.getX() - childEnd.getX()) / 2;
        }
        return ret;
    }

    private double correctRulerX(Point2D parentStart, Point2D childEnd, double rulerX) {
        double ret = 0;

        double left = Math.min(parentStart.getX(), childEnd.getX());
        double right = Math.max(parentStart.getX(), childEnd.getX());

        if (left + RelationshipTopology.MIN_HORIZONTAL_DISTANCE / 2 > rulerX) {
            ret = left + RelationshipTopology.MIN_HORIZONTAL_DISTANCE / 2;
        } else if (right - RelationshipTopology.MIN_HORIZONTAL_DISTANCE / 2 < rulerX) {
            ret = right - RelationshipTopology.MIN_HORIZONTAL_DISTANCE / 2;
        } else {
            ret = rulerX;
        }

        return ret;
    }

    private void addPath(Point2D parentStart, Point2D childEnd, double rulerX, RelationshipType type,
            DirectRelationship shape) {
        Path path = new Path();

        path.getElements().add(new MoveTo(parentStart.getX(), parentStart.getY()));
        path.getElements().add(new HLineTo(rulerX));
        path.getElements().add(new VLineTo(childEnd.getY()));
        path.getElements().add(new HLineTo(childEnd.getX()));

        if (type != RelationshipType.IDENTIFYING) {
            path.getStrokeDashArray().addAll(5d, 5d);
        }

        shape.setPath(path);
    }

    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.DIRECT;
    }
}
