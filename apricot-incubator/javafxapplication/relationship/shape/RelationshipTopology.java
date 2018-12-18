package javafxapplication.relationship.shape;

import javafxapplication.relationship.ApricotRelationship;

public interface RelationshipTopology {
    
    public static final double MIN_HORIZONTAL_DISTANCE = 40;
    public static final double MIN_VERTICAL_DISTANCE = 20;
    
    RelationshipShapeBuilder getRelationshipShapeBuilder(ApricotRelationship relationship);
}
