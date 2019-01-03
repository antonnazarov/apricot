package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.modifiers.ElementVisualModifier;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class HatShapeBuilder extends RelationshipShapeBuilderImpl {

    public static final double HAT_HORIZONTAL_GAP = 30;
    public static final double HAT_VERTICAL_GAP = 10;

    public HatShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder, RelationshipTopology relationshipTopology,
            ElementVisualModifier[] shapeModifiers) {
        super(primitivesBuilder, relationshipTopology, shapeModifiers);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        Side parentSide = relationshipTopology.getRelationshipSide(relationship, true);
        Side childSide = relationshipTopology.getRelationshipSide(relationship, false);
        Point2D parentStart = getParentStart(relationship, parentSide);
        Point2D childEnd = getChildEnd(relationship, childSide);

        HatRelationship shape = new HatRelationship(relationship);

        shape.setLeftRulerX(getDefaultLeftRulerX(parentStart, childEnd));
        shape.setRightRulerX(getDefaultRightRulerX(parentStart, childEnd));
        shape.setCenterRulerY(getDefaultCenterRulerY(relationship));

        addElements(relationship, parentStart, childEnd, parentSide, childSide, shape);
        applyModifiers(shape);

        return shape;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        Side parentSide = relationshipTopology.getRelationshipSide(relationship, true);
        Side childSide = relationshipTopology.getRelationshipSide(relationship, false);
        Point2D parentStart = getParentStart(relationship, parentSide);
        Point2D childEnd = getChildEnd(relationship, childSide);

        if (relationship.getShape() instanceof HatRelationship) {
            HatRelationship shape = (HatRelationship) relationship.getShape();
            correctRulers(parentStart, childEnd, shape, relationship);
            addElements(relationship, parentStart, childEnd, parentSide, childSide, shape);
        }
    }

    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.HAT;
    }

    private void correctRulers(Point2D parentStart, Point2D childEnd, HatRelationship shape,
            ApricotRelationship relationship) {
        Point2D left = getPointOnSide(parentStart, childEnd, true);
        Point2D right = getPointOnSide(parentStart, childEnd, false);

        if (left.getX() - HAT_HORIZONTAL_GAP < shape.getLeftRulerX()) {
            shape.setLeftRulerX(left.getX() - HAT_HORIZONTAL_GAP);
        }
        if (right.getX() + HAT_HORIZONTAL_GAP > shape.getRightRulerX()) {
            shape.setRightRulerX(right.getX() + HAT_HORIZONTAL_GAP);
        }
        double highTop = Math.min(
                relationship.getParent().getEntityShape().getLayoutY()
                        + relationship.getParent().getEntityShape().getTranslateY(),
                relationship.getChild().getEntityShape().getLayoutY()
                        + relationship.getChild().getEntityShape().getTranslateY());
        if (highTop - HAT_VERTICAL_GAP < shape.getCenterRulerY()) {
            shape.setCenterRulerY(highTop - HAT_VERTICAL_GAP);
        }
    }

    private double getDefaultLeftRulerX(Point2D parentStart, Point2D childEnd) {
        return Math.min(parentStart.getX(), childEnd.getX()) - HAT_HORIZONTAL_GAP;
    }

    private double getDefaultRightRulerX(Point2D parentStart, Point2D childEnd) {
        return Math.max(parentStart.getX(), childEnd.getX()) + HAT_HORIZONTAL_GAP;
    }

    private double getDefaultCenterRulerY(ApricotRelationship relationship) {
        double ret = 0;
        if (relationship.getParent().getEntityShape() != null && relationship.getChild().getEntityShape() != null) {
            ret = Math.min(
                    relationship.getParent().getEntityShape().getLayoutY()
                            + relationship.getParent().getEntityShape().getTranslateY(),
                    relationship.getChild().getEntityShape().getLayoutY()) - HAT_VERTICAL_GAP;
        }

        return ret;
    }

    private void addElements(ApricotRelationship relationship, Point2D parentStart, Point2D childEnd, Side parentSide,
            Side childSide, HatRelationship shape) {
        addPath(parentStart, childEnd, shape);
        addStartElement(relationship.getRelationshipType(), parentStart, parentSide, shape);
        addEndElement(childEnd, childSide, shape);
        addRulers(parentStart, childEnd, shape);
    }

    private void addPath(Point2D parentStart, Point2D childEnd, HatRelationship shape) {
        Path path = shape.getPath();
        if (path == null) {
            path = new Path();
            shape.setPath(path);
        } else {
            path.getElements().clear();
        }

        Point2D left = getPointOnSide(parentStart, childEnd, true);
        Point2D right = getPointOnSide(parentStart, childEnd, false);

        path.getElements().add(new MoveTo(left.getX(), left.getY()));
        path.getElements().add(new HLineTo(shape.getLeftRulerX()));
        path.getElements().add(new VLineTo(shape.getCenterRulerY()));
        path.getElements().add(new HLineTo(shape.getRightRulerX()));
        path.getElements().add(new VLineTo(right.getY()));
        path.getElements().add(new HLineTo(right.getX()));
    }

    private void addRulers(Point2D parentStart, Point2D childEnd, HatRelationship shape) {
        Point2D left = getPointOnSide(parentStart, childEnd, true);
        Point2D right = getPointOnSide(parentStart, childEnd, false);

        // left ruler
        Shape ruler = shape.getLeftRuler();
        if (ruler == null) {
            ruler = primitivesBuilder.getRuler();
            shape.setLeftRuler(ruler);
        }
        ruler.setLayoutX(shape.getLeftRulerX() - RelationshipPrimitivesBuilderImpl.RULER_LENGTH / 2);
        ruler.setLayoutY(shape.getCenterRulerY() + (left.getY() - shape.getCenterRulerY()) / 2);

        // right ruler
        ruler = shape.getRightRuler();
        if (ruler == null) {
            ruler = primitivesBuilder.getRuler();
            shape.setRightRuler(ruler);
        }
        ruler.setLayoutX(shape.getRightRulerX() - RelationshipPrimitivesBuilderImpl.RULER_LENGTH / 2);
        ruler.setLayoutY(shape.getCenterRulerY() + (right.getY() - shape.getCenterRulerY()) / 2);

        // center ruler
        ruler = shape.getCenterRuler();
        if (ruler == null) {
            ruler = primitivesBuilder.getRuler();
            shape.setCenterRuler(ruler);
        }
        ruler.setLayoutX(shape.getLeftRulerX() + (shape.getRightRulerX() - shape.getLeftRulerX()) / 2);
        ruler.setLayoutY(shape.getCenterRulerY() - RelationshipPrimitivesBuilderImpl.RULER_LENGTH / 2);
    }

    private Point2D getPointOnSide(Point2D parentStart, Point2D childEnd, boolean isLeft) {
        Point2D ret = null;
        if (isLeft) {
            ret = parentStart.getX() < childEnd.getX() ? parentStart : childEnd;
        } else {
            ret = parentStart.getX() > childEnd.getX() ? parentStart : childEnd;
        }

        return ret;
    }
}
