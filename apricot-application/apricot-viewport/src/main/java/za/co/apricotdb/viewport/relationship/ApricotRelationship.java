package za.co.apricotdb.viewport.relationship;

import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.shape.RelationshipShapeType;

/**
 * The interface of the graphical represenation of the Apricot Relationship on the Canvas.
 *
 * @author Anton Nazarov
 * @version 1.1, changed on 18/12/2020
 */
public interface ApricotRelationship extends ApricotElement {
    
    ApricotEntity getParent(); 
    
    ApricotEntity getChild();

    String getPrimaryKeyName();

    String getForeignKeyName();
    
    RelationshipType getRelationshipType();
    
    RelationshipShapeType getRelationshipShapeType();
    
    String getRelationshipName();
    
    long getRelationshipId();

    void setValid(boolean valid);

    boolean isValid();

    void setValidationMessage(String message);

    String getValidationMessage();

    void setConstraintName(String constraintName);

    String getConstraintName();
}
