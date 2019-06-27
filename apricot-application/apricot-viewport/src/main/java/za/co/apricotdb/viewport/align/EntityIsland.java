package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private EntityIsland master;
    private boolean parent;

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

    public EntityIsland getMaster() {
        return master;
    }

    public void setMaster(EntityIsland master) {
        this.master = master;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public void merge(EntityIsland island, boolean isParent) {
        island.setParent(isParent);
        island.setMaster(this);
        island.removeEntity(core);
        removeEntity(island.getCore());
        merged.add(island);
    }

    public List<EntityIsland> getMergedIslands() {
        return merged;
    }

    public int getIslandRank() {
        int mergedRank = 0;
        for (EntityIsland isl : merged) {
            mergedRank += isl.getIslandRank();
        }
        return parents.size() + children.size() + mergedRank + 1;
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
        return getRelatedEntities().contains(entity);
    }

    /**
     * Collect and return all entities in the island.
     */
    public List<ApricotEntity> getAllEntities() {
        List<ApricotEntity> ret = new ArrayList<>();
        ret.add(core);
        ret.addAll(getRelatedEntities());
        merged.forEach(isl -> ret.addAll(isl.getAllEntities()));

        return ret;
    }

    public Set<ApricotEntity> findDuplicates() {
        List<ApricotEntity> all = getAllEntities();
        Set<ApricotEntity> ret = new HashSet<>();
        Set<ApricotEntity> cp = new HashSet<>();

        for (ApricotEntity ent : all) {
            if (!cp.add(ent)) {
                ret.add(ent);
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Island: ").append(core).append("; parents=").append(parents).append("; children=").append(children);
        if (merged.size() > 0) {
            sb.append("; merged:");
            merged.forEach(mrg -> {
                sb.append(" *").append(mrg.getCore()).append(" (");
                if (parent) {
                    sb.append("prnt, ");
                } else {
                    sb.append("chld, ");
                }
                sb.append(parents.size()).append("/").append(children.size()).append(")");
            });
        }
        Set<ApricotEntity> dups = findDuplicates();
        if (!dups.isEmpty()) {
            sb.append("; dups: ").append(dups);
        } else {
            sb.append("; [NO DUPS]");
        }
        sb.append("; rank=").append(getIslandRank());
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
