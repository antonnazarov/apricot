package za.co.apricotdb.viewport.relationship.shape;

import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class HatRelationship extends ApricotRelationshipShape {

    public HatRelationship(ApricotRelationship relationship) {
        super(relationship, RelationshipShapeType.HAT);
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
