package za.co.apricotdb.viewport.relationship.shape;

import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class RoofRelationship extends ApricotRelationshipShape {

    public RoofRelationship(ApricotRelationship relationship) {
        super(relationship, RelationshipShapeType.ROOF);
    }

    @Override
    public void translateRelationshipRulers(double translateX, double translateY) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetRelationshipRulers() {
        // TODO Auto-generated method stub
        
    }

}
