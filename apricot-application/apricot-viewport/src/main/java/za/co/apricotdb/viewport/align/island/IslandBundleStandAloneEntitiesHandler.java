package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

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
}
