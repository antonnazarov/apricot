package za.co.apricotdb.viewport.relationship;

import java.util.Comparator;

import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.shape.RelationshipShapeType;
import za.co.apricotdb.viewport.relationship.shape.TopologyHelper;

public class RelationshipsInNonIdentifyingStackComparator implements Comparator<ApricotRelationship> {

    @Override
    public int compare(ApricotRelationship ar1, ApricotRelationship ar2) {
        
        if (ar1.getRelationshipType() == RelationshipType.IDENTIFYING && ar2.getRelationshipType() != RelationshipType.IDENTIFYING) {
            return -1;
        } else if (ar1.getRelationshipType() != RelationshipType.IDENTIFYING && ar2.getRelationshipType() == RelationshipType.IDENTIFYING) {
            return 1;
        }
        
        double childY1 = getY(ar1, true);
        double childY2 = getY(ar2, true);
        double parentY = getY(ar1, false);
        RelationshipShapeType sht1 = ar1.getRelationshipShapeType();
        RelationshipShapeType sht2 = ar2.getRelationshipShapeType();
        if (sht1 == RelationshipShapeType.HAT && sht2 != RelationshipShapeType.HAT) {
            return -1;
        } else if (sht1 != RelationshipShapeType.HAT && sht2 == RelationshipShapeType.HAT) {
            return 1;
        } else if (sht1 == RelationshipShapeType.HAT && sht2 == RelationshipShapeType.HAT) {
            return (int) (childY1 - childY2); 
        } else if (sht1 == RelationshipShapeType.DADS_HAND && sht2 == RelationshipShapeType.DADS_HAND) {
            if ((childY1 < parentY && childY2 < parentY) || (childY1 > parentY && childY2 > parentY)) {
                return (int) (childY2 - childY1);
            } else if (childY1 < parentY && childY2 > parentY) {
                return (int) (childY1 - childY2);
            } else if (childY1 > parentY && childY2 < parentY) {
                return (int) (childY1 - childY2);
            }
        } else if (sht1 == RelationshipShapeType.DADS_HAND && sht2 != RelationshipShapeType.DADS_HAND) {
            return (int) (childY1 - parentY);
        } else if (sht1 != RelationshipShapeType.DADS_HAND && sht2 == RelationshipShapeType.DADS_HAND) {
            return (int) (parentY - childY2);
        } else if (sht1 == RelationshipShapeType.DIRECT || sht2 == RelationshipShapeType.DIRECT) {
            return (int) (childY1 - childY2);
        }
        
        return 0;
    }
    
    private double getY(ApricotRelationship relationship, boolean isChildSide) {
        ApricotEntity entity = null;
        String fieldName = null;
        if (isChildSide) {
            entity = relationship.getChild();
            fieldName = relationship.getForeignKeyName();
        } else {
            entity = relationship.getParent();
            fieldName = relationship.getPrimaryKeyName();
        }
        
        return TopologyHelper.getFieldY(entity, fieldName);
    }
}
