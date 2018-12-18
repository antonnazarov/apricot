package javafxapplication.relationship.shape;

import javafxapplication.relationship.ApricotRelationship;

public interface RelationshipShapeBuilder {
    
    ApricotRelationshipShape buildRelationshipShape(ApricotRelationship relationship);
    
    void alterExistingRelationshipShape(ApricotRelationship relationship);
    
    RelationshipShapeType getShapeType();

}
