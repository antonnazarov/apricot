package za.co.apricotdb.ui.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * This class is responsible for consistency of the database schema after the
 * reengineering.
 * 
 * @author Anton Nazarov
 * @since 19/02/2019
 */
@Component
public class ConsistencyHandler {

    public List<ApricotTable> findChildrenForTable(ApricotTable table, List<ApricotRelationship> relationships) {
        List<ApricotTable> ret = new ArrayList<>();

        for (ApricotRelationship r : relationships) {
            // excluding the auto- relationships
            if (!r.getParent().getTable().getName().equals(r.getChild().getTable().getName())
                    && r.getParent().getTable().getName().equals(table.getName())) {
                ret.add(r.getChild().getTable());
            }
        }

        return ret;
    }

    public List<ApricotTable> findParentsForTable(ApricotTable table, List<ApricotRelationship> relationships) {
        List<ApricotTable> ret = new ArrayList<>();

        for (ApricotRelationship r : relationships) {
            // excluding the auto- relationships
            if (!r.getParent().getTable().getName().equals(r.getChild().getTable().getName())
                    && r.getChild().getTable().getName().equals(table.getName())) {
                ret.add(r.getParent().getTable());
            }
        }

        return ret;
    }

    public List<ApricotRelationship> getRelationshipsForTables(List<ApricotTable> tables,
            List<ApricotRelationship> relationships) {
        List<ApricotRelationship> ret = new ArrayList<>();

        Set<String> tableNames = new HashSet<>();
        for (ApricotTable tb : tables) {
            tableNames.add(tb.getName());
        }

        for (ApricotTable t : tables) {
            for (ApricotRelationship r : relationships) {
                if ((t.getName().equals(r.getParent().getTable().getName())
                        || t.getName().equals(r.getChild().getTable().getName()))
                        && tableNames.contains(r.getParent().getTable().getName())
                        && tableNames.contains(r.getChild().getTable().getName())) {
                    if (!ret.contains(r)) {
                        ret.add(r);
                    }
                }
            }
        }

        return ret;
    }

    /**
     * This method searches the extra tables (if any), which need to be excluded in
     * sake of consistency. Returns tables, which need to be excluded in addition to
     * the already presented in the "excluded" list. In the Map<T1, T2>: T1 - the
     * child table to be extra excluded; T2 - the parent table, excluded and caused
     * the exclusion of the child one.
     */
    public Map<ApricotTable, ApricotTable> getFullConsistentExclude(List<ApricotTable> excluded,
            List<ApricotRelationship> relationships) {
        Map<ApricotTable, ApricotTable> ret = new HashMap<>();
        
        Map<ApricotTable, ApricotTable> result = getConsistentExclude(excluded, relationships);
        while (!result.isEmpty()) {
            ret.putAll(result);
            List<ApricotTable> newExcluded = new ArrayList<>(excluded);
            Set<ApricotTable> childen = result.keySet();
            for (ApricotTable t : childen) {
                newExcluded.add(t);
            }
            result = getConsistentExclude(newExcluded, relationships);
        }

        return ret;
    }
    
    private Map<ApricotTable, ApricotTable> getConsistentExclude(List<ApricotTable> excluded,
            List<ApricotRelationship> relationships) {
        Map<ApricotTable, ApricotTable> ret = new HashMap<>();

        for (ApricotTable t : excluded) {
            List<ApricotTable> children = findChildrenForTable(t, relationships);
            for (ApricotTable c : children) {
                if (!excluded.contains(c)) {
                    ret.put(c, t); // this child (c) needs to be excluded because of (t)
                }
            }
        }

        return ret;
    }
}
