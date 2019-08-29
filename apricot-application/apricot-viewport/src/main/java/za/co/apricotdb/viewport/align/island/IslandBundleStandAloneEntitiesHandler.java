package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.align.EntityAllocation;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

@Component
public class IslandBundleStandAloneEntitiesHandler {

    public void handleStandAloneEntites(EntityIslandBundle bundle) {
        bundle.getStandAloneEntities().clear();
        List<EntityIsland> unlinked = getUnlinkedEntities(bundle);
        bundle.getStandAloneEntities().addAll(unlinked);
        bundle.getIslands().removeAll(unlinked);
    }

    /**
     * Get all unlinked entities (the islands with rank=1).
     */
    public List<EntityIsland> getUnlinkedEntities(EntityIslandBundle bundle) {
        List<EntityIsland> ret = new ArrayList<>();
        for (EntityIsland island : bundle.getIslands()) {
            if (island.getIslandRank() == 1) {
                ret.add(island);
            }
        }

        return ret;
    }

    /**
     * After removal of the duplicated entities, some islands of rank=1 might be not
     * included in any of the high rank islands. In this case, the "stand alone"
     * island will be reassigned to one of the related islands of the higher rank.
     */
    public void reattachLinkedStandAlones(List<EntityIsland> unlinked, EntityIslandBundle bundle) {
        for (EntityIsland isl : unlinked) {
            EntityAllocation alloc = isl.getCore();
            boolean included = false;
            outerloop: for (EntityIsland highRankIsl : bundle.getIslands()) {
                if (highRankIsl.getIslandRank() > 1) {
                    for (EntityAllocation a : highRankIsl.getAllEntities()) {
                        if (alloc.equals(a)) {
                            included = true;
                            break outerloop;
                        }
                    }
                }
            }

            if (!included) {
                // include the entity of the island (isl) into one of the related islands
                reattachEntityAllocation(alloc, bundle.getIslands());
            }
        }
    }

    private void reattachEntityAllocation(EntityAllocation alloc, List<EntityIsland> islands) {
        List<EntityIsland> reversed = new ArrayList<>(islands);
        Collections.reverse(reversed);

        for (EntityIsland isl : reversed) {
            if (isl.getIslandRank() > 1) {
                boolean linked = false;
                outerloop: for (EntityAllocation a : isl.getAllEntities()) {
                    for (ApricotRelationship r : a.getPrimaryLinks()) {
                        if (r.getChild().getTableName().equals(alloc.getTableName())) {
                            linked = true;
                            break outerloop;
                        }
                    }
                    for (ApricotRelationship r : a.getForeignLinks()) {
                        if (r.getParent().getTableName().equals(alloc.getTableName())) {
                            linked = true;
                            break outerloop;
                        }
                    }
                }

                if (linked) {
                    isl.getChildren().add(alloc);
                    break;
                }
            }
        }
    }
}
