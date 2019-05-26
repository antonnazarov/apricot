package za.co.apricotdb.viewport.relationship;

import java.util.Comparator;

import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

/**
 * This comparator helps to sort the "incoming" stack- relationships.
 * 
 * @author Anton Nazarov
 * @since 23/05/2019
 *
 */
public class ChildRelationshipStackComparator implements Comparator<ApricotRelationship> {

    @Override
    public int compare(ApricotRelationship r1, ApricotRelationship r2) {
        return Double.compare(getParentY(r1), getParentY(r2));
    }

    private double getParentY(ApricotRelationship r) {
        ApricotEntity entity = r.getParent();
        ApricotEntityShape shape = entity.getEntityShape();

        return shape.getLayoutY();
    }
}
