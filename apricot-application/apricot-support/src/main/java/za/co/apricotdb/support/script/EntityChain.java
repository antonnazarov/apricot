package za.co.apricotdb.support.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityChain {

    private Map<String, Entity> chain = new HashMap<>();
    private int chainLength = 0;
    private List<Entity> deadLoopEntities;

    public boolean addEntity(Entity e) {
        if (chain.get(e.getName()) != null) {
            return false;
        }
        chain.put(e.getName(), e);

        return true;
    }

    public boolean addRelationship(String parent, String child) {
        Entity pe = chain.get(parent);
        Entity ce = chain.get(child);
        if (pe != null && ce != null) {
            pe.addChild(ce);
            ce.addParent(pe);

            return true;
        }

        return false;
    }

    public Entity getEntity(String name) {
        return chain.get(name);
    }

    public List<Entity> sortEntities() {
        return sortEntities(new ArrayList<Entity>(chain.values()));
    }

    public List<Entity> getDeadLoopEntities() {
        deadLoopEntities = new ArrayList<>();
        List<Entity> sorted = sortEntities(new ArrayList<Entity>(chain.values()));
        if (sorted.size() < chain.values().size()) {
            return deadLoopEntities;
        }

        return null;
    }

    private List<Entity> sortEntities(List<Entity> entities) {
        List<Entity> sortedChain = new ArrayList<>();

        List<Entity> pchld = getParentChildEntities(entities);
        if (pchld.size() != chainLength) {
            chainLength = pchld.size();
        } else {
            deadLoopEntities = pchld;
            return sortedChain;
        }

        if (pchld.size() > 0) {
            List<Entity> subchain = createSubChain(pchld);
            sortedChain = sortEntities(subchain);
        }

        List<Entity> result = new ArrayList<>();
        result.addAll(getParentEntities(entities));
        result.addAll(sortedChain);
        result.addAll(getChildEntities(entities));
        result.addAll(getStandaloneEntities(entities));

        return getOriginalEntities(result);
    }

    private List<Entity> getOriginalEntities(List<Entity> result) {
        List<Entity> ret = new ArrayList<>();
        for (Entity e : result) {
            ret.add(chain.get(e.getName()));
        }

        return ret;
    }

    private List<Entity> createSubChain(List<Entity> entities) {
        List<Entity> subchain = new ArrayList<>();

        for (Entity e : entities) {
            Entity ent = new Entity(e.getName());
            for (Entity c : e.getChildren()) {
                if (entities.contains(c)) {
                    ent.addChild(c);
                }
            }
            for (Entity p : e.getParents()) {
                if (entities.contains(p)) {
                    ent.addParent(p);
                }
            }
            subchain.add(ent);
        }

        return subchain;
    }

    /**
     * Get the Entities, which don't have any parents or children.
     */
    private List<Entity> getStandaloneEntities(List<Entity> entities) {
        List<Entity> ret = new ArrayList<>();

        for (Entity e : entities) {
            if (e.getParents().size() == 0 && e.getChildren().size() == 0) {
                ret.add(e);
            }
        }

        return ret;
    }

    /**
     * Get Entities, which are Parents only.
     */
    private List<Entity> getParentEntities(List<Entity> entities) {
        List<Entity> ret = new ArrayList<>();

        for (Entity e : entities) {
            if (e.getParents().size() == 0 && e.getChildren().size() != 0) {
                ret.add(e);
            }
        }

        return ret;
    }

    /**
     * Get Entities, which are Children only.
     */
    private List<Entity> getChildEntities(List<Entity> entities) {
        List<Entity> ret = new ArrayList<>();

        for (Entity e : entities) {
            if (e.getParents().size() != 0 && e.getChildren().size() == 0) {
                ret.add(e);
            }
        }

        return ret;
    }

    /**
     * Get Entities, which are Parents and Children in the same time.
     */
    private List<Entity> getParentChildEntities(List<Entity> entities) {
        List<Entity> ret = new ArrayList<>();

        for (Entity e : entities) {
            if (e.getParents().size() != 0 && e.getChildren().size() != 0) {
                ret.add(e);
            }
        }

        return ret;
    }
}
