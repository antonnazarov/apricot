package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.align.EntityAllocation;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * This is a handler of the stand alone islands (the islands, which do not have
 * any relationship with one ones).
 * 
 * @author Anton Nazarov
 * @since 18/08/2019
 */
@Component
public class IslandBundleStandAloneIslandsHandler {
    
    public void handleStandAloneIslands(EntityIslandBundle bundle) {
        List<EntityIsland> standAlones = new ArrayList<>();
        
        for (EntityIsland isl : bundle.getIslands()) {
            List<IslandRelationship> rels = getIslandRelationships(isl, bundle);
            if (rels.size() == 0) {
                standAlones.add(isl);
            }
        }
        
        bundle.getIslands().removeAll(standAlones);
        bundle.getStandAloneIslands().addAll(standAlones);
    }

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
