package javafxapplication.relationship.shape;

import javafx.geometry.Point2D;
import javafxapplication.relationship.ApricotRelationship;

public class HatShapeBuilder extends RelationshipShapeBuilderImpl {

    public HatShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder) {
        super(primitivesBuilder);
    }

    @Override
    public ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void alterExistingRelationshipShape(ApricotRelationship relationship) {
        // TODO Auto-generated method stub

    }
    
    @Override
    public RelationshipShapeType getShapeType() {
        return RelationshipShapeType.HAT;
    }

    @Override
    protected Point2D getParentStart(ApricotRelationship relationship) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Point2D getChildEnd(ApricotRelationship relationship) {
        // TODO Auto-generated method stub
        return null;
    }
}
