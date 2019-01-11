package za.co.apricotdb.viewport.relationship.shape;

import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public interface RelationshipShapeBuilder {
    
    ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship);
    
    void alterExistingRelationshipShape(ApricotRelationship relationship);
    
    RelationshipShapeType getShapeType();

}
