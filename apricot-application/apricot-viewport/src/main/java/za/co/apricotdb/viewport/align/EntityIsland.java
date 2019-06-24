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
public class EntityIsland {
    
    private ApricotEntity core;
    private List<ApricotEntity> parents;
    private List<ApricotEntity> children;

    public EntityIsland(ApricotEntity core) {
        this.core = core;
        parents = new ArrayList<>();
        children = new ArrayList<>();
        
        for (ApricotRelationship r : core.getForeignLinks()) {
            parents.add(r.getParent());
        }
        for (ApricotRelationship r : core.getPrimaryLinks()) {
            children.add(r.getParent());
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Island: ").append(core)
        .append(", parents=").append(parents)
        .append(", children=").append(children);
        
        return sb.toString();
    }
}
