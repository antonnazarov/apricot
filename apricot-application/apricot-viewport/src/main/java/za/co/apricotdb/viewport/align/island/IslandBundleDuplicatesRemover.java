package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.align.EntityAllocation;

@Component
public class IslandBundleDuplicatesRemover {
    /**
     * Scan islands and remove duplicate relationships.
     */
    public void removeDuplicates(EntityIslandBundle bundle) {
        List<EntityIsland> tmpIslands = new ArrayList<>(bundle.getIslands());
        bundle.getIslands().clear();
        while (tmpIslands.size() > 0) {
            EntityIsland current = tmpIslands.get(0);
            bundle.getIslands().add(current);
            tmpIslands.remove(current);
            for (EntityIsland island : tmpIslands) {
                for (EntityAllocation ent : current.getRelatedEntities()) {
                    island.removeEntity(ent);
                }
                island.removeEntity(current.getCore());
            }
        }
    }
}
