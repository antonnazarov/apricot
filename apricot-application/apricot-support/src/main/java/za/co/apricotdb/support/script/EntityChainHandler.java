package za.co.apricotdb.support.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The Spring component which provides access to the EntityChain functionality.
 * 
 * @author Anton Nazarov
 * @since 02/04/2019
 */
@Component
public class EntityChainHandler {

    public List<ApricotTable> getParentChildChain(List<ApricotTable> tables, List<ApricotRelationship> relationships) {
        EntityChain chain = buidEntityChain(tables, relationships);
        
        //  sort entities
        List<Entity> sortedEntities = chain.sortEntities();
        return getTablesFromEntities(tables, sortedEntities);
    }

    public List<ApricotTable> getDeadLoopTables(List<ApricotTable> tables, List<ApricotRelationship> relationships) {
        List<ApricotTable> deadLoopTables = new ArrayList<>();
        EntityChain chain = buidEntityChain(tables, relationships);
        chain.sortEntities();
        List<Entity> deadLoopEntities = chain.getDeadLoopEntities();
        if (deadLoopEntities != null && deadLoopEntities.size() > 0) {
            deadLoopTables = getTablesFromEntities(tables, deadLoopEntities);
        }
        
        return deadLoopTables;
    }

    public List<ApricotTable> getChildParentChain(List<ApricotTable> tables, List<ApricotRelationship> relationships) {
        List<ApricotTable> sortedResult = getParentChildChain(tables, relationships);
        if (sortedResult != null) {
            Collections.reverse(sortedResult);
        }

        return sortedResult;
    }

    private List<ApricotTable> getTablesFromEntities(List<ApricotTable> tables, List<Entity> entities) {
        List<ApricotTable> ret = new ArrayList<>();
        Map<String, ApricotTable> tMap = getTableMap(tables);
        for (Entity e : entities) {
            ApricotTable t = tMap.get(e.getName());
            if (t != null) {
                ret.add(t);
            }
        }

        return ret;
    }

    private Map<String, ApricotTable> getTableMap(List<ApricotTable> tables) {
        Map<String, ApricotTable> tablesMap = new HashMap<>();
        for (ApricotTable t : tables) {
            tablesMap.put(t.getName(), t);
        }

        return tablesMap;
    }

    private EntityChain buidEntityChain(List<ApricotTable> tables, List<ApricotRelationship> relationships) {
        EntityChain chain = new EntityChain();
        for (ApricotTable t : tables) {
            chain.addEntity(new Entity(t.getName()));
        }

        for (ApricotRelationship r : relationships) {
            chain.addRelationship(r.getParent().getTable().getName(), r.getChild().getTable().getName());
        }

        return chain;
    }
}
