package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This is a handler of the stand alone islands (the islands, which do not have
 * any relationship with one ones).
 * 
 * @author Anton Nazarov
 * @since 18/08/2019
 */
@Component
public class IslandBundleStandAloneIslandsHandler {

    @Autowired
    IslandRelationshipHandler relationshipHandler;

    public void handleStandAloneIslands(EntityIslandBundle bundle) {
        List<EntityIsland> standAlones = new ArrayList<>();

        for (EntityIsland isl : bundle.getIslands()) {
            List<IslandRelationship> rels = relationshipHandler.getIslandRelationships(isl, bundle);
            if (rels.size() == 0) {
                standAlones.add(isl);
            }
        }

        bundle.getIslands().removeAll(standAlones);
        bundle.getStandAloneIslands().addAll(standAlones);
    }

}
