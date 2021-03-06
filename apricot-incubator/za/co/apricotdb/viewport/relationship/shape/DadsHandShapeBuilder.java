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

/**
 * This shape builder creates relationships of type DadsHandRelationship.
 * 
 * @author Anton Nazarov
 * @since 28/12/2018
 */
public class DadsHandShapeBuilder extends RelationshipShapeBuilderImpl {

    public static final double DH_HORIZONTAL_GAP = 30;
    public static final double DH_VERTICAL_GAP = 10;

    public DadsHandShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder,
            RelationshipTopology relationshipTopology, ElementVisualModifier[] shapeModifiers) {
        super(primitivesBuilder, relationshipTopology, shapeModifiers);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        Side parentSide = relationshipTopology.getRelationshipSide(relationship, true);
        Side childSide = relationshipTopology.getRelationshipSide(relationship, false);
        Point2D parentStart = getParentStart(relationship, parentSide);
        Point2D childEnd = getChildEnd(relationship, childSide);

        DadsHandRelationship shape = new DadsHandRelationship(relationship);
        shape.setRulerX(getDefaultRulerX(relationship, parentSide));

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

        if (relationship.getShape() instanceof DadsHandRelationship) {
            DadsHandRelationship shape = (DadsHandRelationship) relationship.getShape();
            correctRulerX(relationship, parentSide, shape);
            addElements(relationship, parentStart, childEnd, parentSide, childSide, shape);
        }
    }

    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.DADS_HAND;
    }

    private void addElements(ApricotRelationship relationship, Point2D parentStart, Point2D childEnd, Side parentSide,
            Side childSide, DadsHandRelationship shape) {
        addPath(parentStart, childEnd, shape);
        addStartElement(relationship.getRelationshipType(), parentStart, parentSide, shape);
        addEndElement(childEnd, childSide, shape);
        addRuler(parentStart, childEnd, shape);
    }

    private void addPath(Point2D parentStart, Point2D childEnd, DadsHandRelationship shape) {
        Path path = shape.getPath();
        if (path == null) {
            path = new Path();
            shape.setPath(path);
        } else {
            path.getElements().clear();
        }
        
        path.getElements().add(new MoveTo(parentStart.getX(), parentStart.getY()));
        path.getElements().add(new HLineTo(shape.getRulerX()));
        path.getElements().add(new VLineTo(childEnd.getY()));
        path.getElements().add(new HLineTo(childEnd.getX()));
    }

    private void addRuler(Point2D parentStart, Point2D childEnd, DadsHandRelationship shape) {
        Shape ruler = shape.getRuler();
        if (ruler == null) {
            ruler = primitivesBuilder.getRuler();
            shape.setRuler(ruler);
        }
        ruler.setLayoutX(shape.getRulerX() - RelationshipPrimitivesBuilderImpl.RULER_LENGTH / 2);
        double minY = Math.min(parentStart.getY(), childEnd.getY());
        ruler.setLayoutY(minY + Math.abs(parentStart.getY() - childEnd.getY()) / 2);
    }

    private double getDefaultRulerX(ApricotRelationship relationship, Side parentSide) {
        double ret = 0;

        if (parentSide == Side.RIGHT) {
            ret = TopologyHelper.getExtremeXPosition(relationship, false) + DH_HORIZONTAL_GAP;
        } else {
            ret = TopologyHelper.getExtremeXPosition(relationship, true) - DH_HORIZONTAL_GAP;
        }

        return ret;
    }

    private void correctRulerX(ApricotRelationship relationship, Side parentSide, DadsHandRelationship shape) {
        if (parentSide == Side.RIGHT) {
            if (shape.getRulerX() < TopologyHelper.getExtremeXPosition(relationship, false) + DH_HORIZONTAL_GAP) {
                shape.setRulerX(TopologyHelper.getExtremeXPosition(relationship, false) + DH_HORIZONTAL_GAP);
            }
        } else {
            if (shape.getRulerX() > TopologyHelper.getExtremeXPosition(relationship, true) - DH_HORIZONTAL_GAP) {
                shape.setRulerX(TopologyHelper.getExtremeXPosition(relationship, true) - DH_HORIZONTAL_GAP);
            }
        }
    }
}
