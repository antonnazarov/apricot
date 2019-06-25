package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * The Entity Island holder.
 * 
 * @author Anton Nazarov
 * @since 24/06/2019
 */
public class EntityIsland implements Comparable<EntityIsland> {

    private ApricotEntity core;
    private List<ApricotEntity> parents;
    private List<ApricotEntity> children;
    private List<EntityIsland> merged; // merged islands

    public EntityIsland(ApricotEntity core) {
        this.core = core;
        parents = new ArrayList<>();
        children = new ArrayList<>();
        merged = new ArrayList<>();

        for (ApricotRelationship r : core.getForeignLinks()) {
            if (!r.getParent().equals(core) && !parents.contains(r.getParent())) {
                parents.add(r.getParent());
            }
        }

        for (ApricotRelationship r : core.getPrimaryLinks()) {
            if (!r.getChild().equals(core) && !children.contains(r.getChild())) {
                children.add(r.getChild());
            }
        }
    }

    public void merge(EntityIsland island) {
        island.removeEntity(core);
        merged.add(island);
    }

    public List<EntityIsland> getMergedIslands() {
        return merged;
    }

    public int getIslandRank() {
        return parents.size() + children.size();
    }

    public ApricotEntity getCore() {
        return core;
    }

    public List<ApricotEntity> getParents() {
        return parents;
    }

    public List<ApricotEntity> getChildren() {
        return children;
    }

    public void removeEntity(ApricotEntity entity) {
        if (parents.contains(entity)) {
            parents.remove(entity);
        }
        if (children.contains(entity)) {
            children.remove(entity);
        }
    }

    public List<ApricotEntity> getRelatedEntities() {
        List<ApricotEntity> ret = new ArrayList<>(children);
        ret.addAll(parents);

        return ret;
    }

    public boolean isLinkedTo(ApricotEntity entity) {
        for (ApricotEntity relEntity : getRelatedEntities()) {
            if (relEntity.equals(entity)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Island: ").append(core).append("; parents=").append(parents).append("; children=").append(children)
                .append("; rank=").append(getIslandRank());
        if (merged.size() > 0) {
            sb.append("; merged: ").append(merged);
        }
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((core == null) ? 0 : core.hashCode());
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
        EntityIsland other = (EntityIsland) obj;
        if (core == null) {
            if (other.core != null) {
                return false;
            }
        } else if (!core.equals(other.core)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(EntityIsland other) {
        if (this.getIslandRank() == other.getIslandRank()) {
            return other.children.size() - this.children.size();
        }
        return (other.getIslandRank() - this.getIslandRank());
    }
}
