package za.co.apricotdb.viewport.relationship.shape;

import javafx.geometry.Side;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public interface RelationshipTopology {
    
    public static final double MIN_HORIZONTAL_DISTANCE = 40;
    public static final double MIN_VERTICAL_DISTANCE = 20;
    
    RelationshipShapeBuilder getRelationshipShapeBuilder(ApricotRelationship relationship);
    
    Side getRelationshipSide(ApricotRelationship relationship, boolean isPrimaryKey);
}
