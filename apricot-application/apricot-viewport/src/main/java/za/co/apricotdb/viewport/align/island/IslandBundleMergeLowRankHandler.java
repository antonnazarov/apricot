package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This handler selects the low range related islands and merge them into the
 * higher rank islands.
 * 
 * @author Anton Nazarov
 * @since 30/08/2019
 */
@Component
public class IslandBundleMergeLowRankHandler {

    @Autowired
    IslandRelationshipHandler relationshipHandler;

    public void mergeLowRankIslands(EntityIslandBundle bundle) {
        List<EntityIsland> lowRankIslands = new ArrayList<>();

        for (EntityIsland isl : bundle.getIslands()) {
            if (isl.getIslandRank() <= 3) {
                lowRankIslands.add(isl);
            }
        }

        for (EntityIsland isl : lowRankIslands) {
            if (isl.getIslandRank() <= 3) {
                List<IslandRelationship> rls = relationshipHandler.getIslandRelationships(isl, bundle);
                IslandRelationship defRel = IslandRelationship.getTopRelationship(rls);
                EntityIsland relIsl = defRel.getRelatedIsland();
                relIsl.merge(isl, false);
                bundle.getIslands().remove(isl);
            }
        }
    }
}
