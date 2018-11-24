package javafxapplication.relationship;

import javafxapplication.entity.ApricotEntity;

public interface ApricotEntityLink {

    ApricotEntity getParent();

    ApricotEntity getChild();
    
    double getPrimaryFieldLayoutY();
    
    double getForeignFieldLayoutY();
    
    EntityLinkBuilder getLinkBuilder();
    
    RelationshipType getRelationshipType();
}
