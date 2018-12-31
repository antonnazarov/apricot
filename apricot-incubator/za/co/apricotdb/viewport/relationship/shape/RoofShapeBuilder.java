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

public class RoofShapeBuilder extends RelationshipShapeBuilderImpl {
    
    public static final double ROOF_VERTICAL_GAP = 20;

    public RoofShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder, RelationshipTopology relationshipTopology,
            ElementVisualModifier[] shapeModifiers) {
        super(primitivesBuilder, relationshipTopology, shapeModifiers);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        Point2D parentStart = getParentStart(relationship, Side.TOP);
        Point2D childEnd = getChildEnd(relationship, Side.TOP);

        RoofRelationship shape = new RoofRelationship(relationship);

        shape.setRulerY(getDefaultRulerY(parentStart, childEnd));

        addElements(relationship, parentStart, childEnd, shape);
        applyModifiers(shape);

        return shape;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        Point2D parentStart = getParentStart(relationship, Side.TOP);
        Point2D childEnd = getChildEnd(relationship, Side.TOP);
        
        if (relationship.getShape() instanceof RoofRelationship) {
            RoofRelationship shape = (RoofRelationship) relationship.getShape();
            correctRulerY(parentStart, childEnd, shape);
            addElements(relationship, parentStart, childEnd, shape);
            applyModifiers(shape);
        }
    }

    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.ROOF;
    }
    
    private void addElements(ApricotRelationship relationship, Point2D parentStart, Point2D childEnd, RoofRelationship shape) {
        addPath(parentStart, childEnd, shape);
        addEndElement(childEnd, Side.TOP, shape);
        addRuler(parentStart, childEnd, shape);
    }

    private void addRuler(Point2D parentStart, Point2D childEnd, RoofRelationship shape) {
        Shape ruler = primitivesBuilder.getRuler();
        ruler.setLayoutY(shape.getRulerY() - RelationshipPrimitivesBuilderImpl.RULER_LENGTH / 2);

        double minX = Math.min(parentStart.getX(), childEnd.getX());
        ruler.setLayoutX(minX + Math.abs(parentStart.getX() - childEnd.getX()) / 2);
        shape.setRuler(ruler);
    }
    
    private double getDefaultRulerY(Point2D parentStart, Point2D childEnd) {
        return Math.min(parentStart.getY(), childEnd.getY()) - ROOF_VERTICAL_GAP;
    }
    
    private void addPath(Point2D parentStart, Point2D childEnd, RoofRelationship shape) {
        Path path = new Path();
        path.getElements().add(new MoveTo(parentStart.getX(), parentStart.getY()));
        path.getElements().add(new VLineTo(shape.getRulerY()));
        path.getElements().add(new HLineTo(childEnd.getX()));
        path.getElements().add(new VLineTo(childEnd.getY()));

        shape.setPath(path);
    }
    
    private void correctRulerY(Point2D parentStart, Point2D childEnd, RoofRelationship shape) {
        double calcY = Math.min(parentStart.getY(), childEnd.getY()) - ROOF_VERTICAL_GAP;
        
        if (calcY < shape.getRulerY()) {
            shape.setRulerY(calcY);
        }
    }
}
