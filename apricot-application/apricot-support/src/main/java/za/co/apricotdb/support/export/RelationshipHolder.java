package za.co.apricotdb.support.export;

/**
 * This is the plain bean to hold the id's of the relationship- including
 * objects.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
public class RelationshipHolder {

    private long parentConstraintId;
    private long childConstraintId;

    public long getParentConstraintId() {
        return parentConstraintId;
    }

    public void setParentConstraintId(long parentConstraintId) {
        this.parentConstraintId = parentConstraintId;
    }

    public long getChildConstraintId() {
        return childConstraintId;
    }

    public void setChildConstraintId(long childConstraintId) {
        this.childConstraintId = childConstraintId;
    }

    @Override
    public String toString() {
        return "RelationshipHolder [parentConstraintId=" + parentConstraintId + ", childConstraintId="
                + childConstraintId + "]";
    }
}
