package javafxapplication.relationship;

import javafxapplication.canvas.ApricotElement;
import javafxapplication.entity.ApricotEntity;

public interface ApricotRelationship extends ApricotElement {
    
    ApricotEntity getParent(); 
    
    ApricotEntity getChild();

    String getPrimaryKeyName();

    String getForeignKeyName();
    
    RelationshipType getRelationshipType();
}
