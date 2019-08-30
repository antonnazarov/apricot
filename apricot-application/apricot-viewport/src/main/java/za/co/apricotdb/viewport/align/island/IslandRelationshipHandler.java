package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.align.EntityAllocation;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * All inter-islands relationships- related logic is provided by this handler. 
 * 
 * @author Anton Nazarov
 * @since 30/08/2019
 */
@Component
public class IslandRelationshipHandler {
    
    /**
     * Get all relationships of the island.
     */
    public List<IslandRelationship> getIslandRelationships(EntityIsland island, EntityIslandBundle bundle) {
        List<IslandRelationship> ret = new ArrayList<>();

        List<ApricotRelationship> entityRels = new ArrayList<>();
        for (EntityAllocation alloc : island.getAllEntities()) {
            entityRels.addAll(alloc.getForeignLinks());
            entityRels.addAll(alloc.getPrimaryLinks());
        }

        Set<IslandRelationship> islRels = new HashSet<>();
        // scan through all relationships of the island
        for (ApricotRelationship r : entityRels) {
            // the whole bundle cycle
            for (EntityIsland isl : bundle.getIslands()) {
                // not itself
                if (!island.equals(isl)) {
                    // scan trough the Entities of the bundle-island
                    for (EntityAllocation alloc : isl.getAllEntities()) {
                        if (alloc.getTableName().equals(r.getParent().getTableName())
                                || alloc.getTableName().equals(r.getChild().getTableName())) {
                            boolean coreRelationship = false;
                            if (island.getCore().getTableName().equals(r.getParent().getTableName())
                                    || island.getCore().getTableName().equals(r.getChild().getTableName())) {
                                coreRelationship = true;
                            }

                            islRels.add(new IslandRelationship(island, isl, coreRelationship));
                        }
                    }
                }
            }
        }
        ret.addAll(islRels);

        return ret;
    }
}
