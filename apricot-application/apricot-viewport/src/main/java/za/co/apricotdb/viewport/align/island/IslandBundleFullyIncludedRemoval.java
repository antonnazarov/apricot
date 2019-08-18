package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.align.EntityAllocation;

@Component
public class IslandBundleFullyIncludedRemoval {
    /**
     * Remove the islands which entities have been fully included into the high rank
     * islands.
     */
    public void removeFullyIncluded(EntityIslandBundle bundle) {
        List<EntityIsland> delete = new ArrayList<>();
        Set<EntityAllocation> allocs = new HashSet<>();

        for (EntityIsland isl : bundle.getIslands()) {
            if (allocs.containsAll(isl.getAllEntities())) {
                delete.add(isl);
            } else {
                allocs.addAll(isl.getAllEntities());
            }
        }

        bundle.getIslands().removeAll(delete);
    }
}
