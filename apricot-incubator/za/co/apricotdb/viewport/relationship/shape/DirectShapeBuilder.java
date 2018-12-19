package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;

public class DirectShapeBuilder extends RelationshipShapeBuilderImpl {

    public DirectShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder) {
        super(primitivesBuilder);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        Point2D parentStart = getParentStart(relationship);
        Point2D childEnd = getChildEnd(relationship);

        DirectRelationship shape = new DirectRelationship(relationship);
        double defaultRulerX = getDefaultRulerX(parentStart, childEnd,
                TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild()));
        shape.setRulerX(defaultRulerX);

        addElements(relationship, parentStart, childEnd, defaultRulerX, shape);

        return shape;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        if (relationship.getShape() != null) {
            Point2D parentStart = getParentStart(relationship);
            Point2D childEnd = getChildEnd(relationship);

            DirectRelationship shape = (DirectRelationship) relationship.getShape();
            double correctedRulerX = correctRulerX(parentStart, childEnd, shape.getRulerX());
            shape.setRulerX(correctedRulerX);

            addElements(relationship, parentStart, childEnd, correctedRulerX, shape);
        }
    }

    private void addElements(ApricotRelationship relationship, Point2D parentStart, Point2D childEnd, double rulerX,
            DirectRelationship shape) {
        boolean isParentLeft = TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild());
        addPath(parentStart, childEnd, rulerX, relationship.getRelationshipType(), shape);
        addStartElement(relationship.getRelationshipType(), parentStart, isParentLeft, shape);
        addEndElement(childEnd, isParentLeft, shape);
        addRuler(parentStart, childEnd, rulerX, shape);
    }

    private void addStartElement(RelationshipType type, Point2D parentStart, boolean isParentLeft,
            DirectRelationship shape) {
        if (type == RelationshipType.OPTIONAL_NON_IDENTIFYING) {
            Shape startElement = primitivesBuilder.getOptionalStart();
            if (isParentLeft) {
                startElement.setLayoutX(parentStart.getX() + 2);
            } else {
                startElement
                        .setLayoutX(parentStart.getX() - (RelationshipPrimitivesBuilderImpl.OPTIONAL_START_LENGTH + 2));
            }
            startElement.setLayoutY(parentStart.getY() - RelationshipPrimitivesBuilderImpl.OPTIONAL_START_LENGTH / 2);
            shape.setStartElement(startElement);
        }
    }

    private void addEndElement(Point2D childEnd, boolean isParentLeft, DirectRelationship shape) {
        Shape endElement = null;
        if (isParentLeft) {
            endElement = primitivesBuilder.getEnd(new Point2D(
                    childEnd.getX() - RelationshipPrimitivesBuilderImpl.LINK_END_DIAMETER, childEnd.getY()));
        } else {
            endElement = primitivesBuilder.getEnd(new Point2D(
                    childEnd.getX() + RelationshipPrimitivesBuilderImpl.LINK_END_DIAMETER, childEnd.getY()));
        }
        shape.setEndElement(endElement);
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

    @Override
    protected Point2D getParentStart(ApricotRelationship relationship) {
        Point2D ret = null;

        VBox pBox = (VBox) relationship.getParent().getShape();
        if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
            ret = new Point2D(pBox.getLayoutX() + pBox.getTranslateX() + pBox.getWidth(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
        } else {
            ret = new Point2D(pBox.getLayoutX() + pBox.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getParent(), relationship.getPrimaryKeyName()));
        }

        return ret;
    }

    @Override
    protected Point2D getChildEnd(ApricotRelationship relationship) {
        Point2D ret = null;

        VBox cBox = (VBox) relationship.getChild().getShape();
        if (TopologyHelper.isParentLeft(relationship.getParent(), relationship.getChild())) {
            ret = new Point2D(cBox.getLayoutX() + cBox.getTranslateX(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
        } else {
            ret = new Point2D(cBox.getLayoutX() + cBox.getTranslateX() + cBox.getWidth(),
                    TopologyHelper.getFieldY(relationship.getChild(), relationship.getForeignKeyName()));
        }

        return ret;
    }
}
