package za.co.apricotdb.ui.handler;

/**
 * This is a bean/holder of the info for the related entity, which is absent on
 * the current view. Used by the "Select Related Entities" functionality.
 * 
 * @author Anton Nazarov
 * @since 13/04/2020
 */
public class RelatedEntityAbsent {

    private String relatedTable;
    private boolean parent;
    private boolean child;
    
    public RelatedEntityAbsent(String relatedTable) {
        this.relatedTable = relatedTable;
    }

    public String getRelatedTable() {
        return relatedTable;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "RelatedEntityAbsent [relatedTable=" + relatedTable + ", parent=" + parent + ", child=" + child + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((relatedTable == null) ? 0 : relatedTable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RelatedEntityAbsent other = (RelatedEntityAbsent) obj;
        if (relatedTable == null) {
            if (other.relatedTable != null) {
                return false;
            }
        } else if (!relatedTable.equals(other.relatedTable)) {
            return false;
        }
        return true;
    }
}
