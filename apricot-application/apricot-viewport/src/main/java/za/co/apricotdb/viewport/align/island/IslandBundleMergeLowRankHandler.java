package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * Collect the low range (<=4) islands and merge them into the linked high rank
 * ones.
 * 
 * @author Anton Nazarov
 * @since 18/08/2019
 */
@Component
public class IslandBundleMergeLowRankHandler {

    public void mergeLowRank(EntityIslandBundle bundle, ApricotCanvas canvas) {

    }

    /**
     * Get all low rank islands.
     */
    private List<EntityIsland> getLowRankIslands(EntityIslandBundle bundle) {
        List<EntityIsland> ret = new ArrayList<>();

        for (EntityIsland isl : bundle.getIslands()) {
            if (isl.getIslandRank() <= 4) {
                ret.add(isl);
            }
        }

        return ret;
    }
}
