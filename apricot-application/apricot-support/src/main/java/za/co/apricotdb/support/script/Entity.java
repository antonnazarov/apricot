package za.co.apricotdb.support.script;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    
    private String name;
    private List<Entity> parents = new ArrayList<>();
    private List<Entity> children = new ArrayList<>();
    
    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    void addParent(Entity e) {
        if (parents.contains(e)) {
            return;
        }
        parents.add(e);
    }
    
    void addChild(Entity e) {
        if (children.contains(e)) {
            return;
        }
        children.add(e);
    }
    
    public List<Entity> getParents() {
        return parents;
    }
    
    public List<Entity> getChildren() {
        return children;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entity other = (Entity) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "entity->[" + name + "]"; 
    }
}
