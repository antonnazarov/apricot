package za.co.apricotdb.persistence.data;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

@Component
public class RelationshipCloneManager {
    
    public ApricotRelationship cloneRelationship(ApricotRelationship relationship, ApricotSnapshot clonedSnapshot) {
        ApricotConstraint clonedParent = getConstraintByName(clonedSnapshot, relationship.getParent().getName());
        ApricotConstraint clonedChild = getConstraintByName(clonedSnapshot, relationship.getChild().getName());
        ApricotRelationship clonedRelationship = new ApricotRelationship(clonedParent, clonedChild);
        
        return clonedRelationship;
    }
    
    private ApricotConstraint getConstraintByName(ApricotSnapshot clonedSnapshot, String constraintName) {
        for (ApricotTable clonedTable : clonedSnapshot.getTables()) {
            for (ApricotConstraint clonedConstraint : clonedTable.getConstraints()) {
                if (constraintName.equals(clonedConstraint.getName())) {
                    return clonedConstraint;
                }
            }
        }
        
        return null;
    }
}
