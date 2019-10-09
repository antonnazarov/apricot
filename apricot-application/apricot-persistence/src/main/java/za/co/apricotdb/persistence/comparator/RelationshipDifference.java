package za.co.apricotdb.persistence.comparator;

import za.co.apricotdb.persistence.entity.ApricotRelationship;

/**
 * The difference between the Apricot Relationships.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public class RelationshipDifference implements ApricotObjectDifference<ApricotRelationship> {
    
    private ApricotRelationship source;
    private ApricotRelationship target;
    
    public RelationshipDifference(ApricotRelationship source, ApricotRelationship target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public ApricotRelationship getSourceObject() {
        return source;
    }

    @Override
    public ApricotRelationship getTargetObject() {
        return target;
    }

    @Override
    public boolean isDifferent() {
        // TODO Auto-generated method stub
        return false;
    }
}
