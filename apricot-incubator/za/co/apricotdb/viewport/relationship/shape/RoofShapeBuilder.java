package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class RoofShapeBuilder extends RelationshipShapeBuilderImpl {

    public RoofShapeBuilder(RelationshipPrimitivesBuilder primitivesBuilder) {
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
        // TODO Auto-generated method stub
        return RelationshipShapeType.ROOF;
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
