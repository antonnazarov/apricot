package za.co.apricotdb.viewport.relationship.shape;

import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public interface RelationshipTopology {
    
    public static final double MIN_HORIZONTAL_DISTANCE = 40;
    public static final double MIN_VERTICAL_DISTANCE = 20;
    
    RelationshipShapeBuilder getRelationshipShapeBuilder(ApricotRelationship relationship);
}
