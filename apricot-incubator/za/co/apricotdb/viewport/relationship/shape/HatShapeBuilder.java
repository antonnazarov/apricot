package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.RelationshipType;

public class HatShapeBuilder extends RelationshipShapeBuilderImpl {

    public HatShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder, RelationshipTopology relationshipTopology) {
        super(primitivesBuilder, relationshipTopology);
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

        return shape;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        // TODO Auto-generated method stub

    }

    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.HAT;
    }

    private double getDefaultLeftRulerX(Point2D parentStart, Point2D childEnd) {
        return Math.min(parentStart.getX(), childEnd.getX()) - 30;
    }

    private double getDefaultRightRulerX(Point2D parentStart, Point2D childEnd) {
        return Math.max(parentStart.getX(), childEnd.getX()) + 30;
    }

    private double getDefaultCenterRulerY(ApricotRelationship relationship) {
        double ret = 0;
        if (relationship.getParent().getShape() != null && relationship.getChild().getShape() != null) {
            ret = Math.min(relationship.getParent().getShape().getLayoutY(),
                    relationship.getChild().getShape().getLayoutY()) - 10;
        }

        return ret;
    }
    
    private void addElements(ApricotRelationship relationship, Point2D parentStart, Point2D childEnd, Side parentSide,
            Side childSide, HatRelationship shape) {
        addPath(parentStart, childEnd, relationship.getRelationshipType(), shape);
        addStartElement(relationship.getRelationshipType(), parentStart, parentSide, shape);
        addEndElement(childEnd, childSide, shape);
        // addPath(parentStart, childEnd, rulerX, relationship.getRelationshipType(), shape);
        // addRuler(parentStart, childEnd, rulerX, shape);
    }
    
    private void addPath(Point2D parentStart, Point2D childEnd, RelationshipType type, HatRelationship shape) {
        Path path = new Path();
        
        Point2D left = parentStart.getX() < childEnd.getX() ? parentStart : childEnd;
        Point2D right = parentStart.getX() > childEnd.getX() ? parentStart : childEnd;
        
        path.getElements().add(new MoveTo(left.getX(), left.getY()));
        path.getElements().add(new HLineTo(shape.getLeftRulerX()));
        path.getElements().add(new VLineTo(shape.getCenterRulerY()));
        path.getElements().add(new HLineTo(shape.getRightRulerX()));
        path.getElements().add(new VLineTo(right.getY()));
        path.getElements().add(new HLineTo(right.getX()));
        
        if (type != RelationshipType.IDENTIFYING) {
            path.getStrokeDashArray().addAll(5d, 5d);
        }

        shape.setPath(path);        
    }
}
