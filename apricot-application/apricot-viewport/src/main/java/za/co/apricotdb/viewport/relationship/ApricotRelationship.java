package za.co.apricotdb.viewport.relationship;

import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.shape.RelationshipShapeType;

public interface ApricotRelationship extends ApricotElement {
    
    ApricotEntity getParent(); 
    
    ApricotEntity getChild();

    String getPrimaryKeyName();

    String getForeignKeyName();
    
    RelationshipType getRelationshipType();
    
    RelationshipShapeType getRelationshipShapeType();
    
    String getRelationshipName();
}
