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

    public EntityIsland(ApricotEntity core) {
        this.core = core;
        parents = new ArrayList<>();
        children = new ArrayList<>();

        for (ApricotRelationship r : core.getForeignLinks()) {
            if (!r.getParent().equals(core)) {
                parents.add(r.getParent());
            }
        }
        for (ApricotRelationship r : core.getPrimaryLinks()) {
            if (!r.getChild().equals(core)) {
                children.add(r.getChild());
            }
        }
    }

    public int getIslandRange() {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Island: ").append(core).append(", parents=").append(parents).append(", children=").append(children).append("\n");

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
        if (this.getIslandRange() == other.getIslandRange()) {
            return other.children.size() - this.children.size();
        }
        return (other.getIslandRange() - this.getIslandRange());
    }
}
