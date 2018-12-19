package za.co.apricotdb.viewport.relationship.shape;

import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class HatRelationship extends ApricotRelationshipShape {

    public HatRelationship(ApricotRelationship relationship) {
        super(relationship, RelationshipShapeType.HAT);
    }
}
