package za.co.apricotdb.viewport.align.island;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This component populates the EntityIsland bundle, using the concrete bundle
 * handling steps.
 * 
 * @author Anton Nazarov
 * @since 18/08/2019
 */
@Component
public class IslandBundleHandler {

    Logger logger = LoggerFactory.getLogger(IslandBundleHandler.class);

    @Autowired
    IslandBundleInitializer bundleInitializer;

    @Autowired
    IslandBundleStandAloneEntitiesHandler standAloneEntitiesHandler;

    @Autowired
    IslandBundleDuplicatesRemover duplicatesRemover;

    @Autowired
    IslandBundleFullyIncludedRemoval fullyIncludedRemoval;

    @Autowired
    IslandBundleMergeRelatedHandler mergeRelatedHandler;
    
    @Autowired
    IslandBundleStandAloneIslandsHandler standAloneIslandsHandler;
    
    @Autowired
    IslandBundleMergeLowRangeHandler mergeLowRangeHandler;

    public EntityIslandBundle createIslandBundle(ApricotCanvas canvas) {
        EntityIslandBundle bundle = new EntityIslandBundle();

        bundleInitializer.initIslands(bundle, canvas);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "ORIGINAL");

        standAloneEntitiesHandler.handleStandAloneEntites(bundle);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "NO STAND ALONES");

        duplicatesRemover.removeDuplicates(bundle);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "REMOVED DUPLICATES");

        List<EntityIsland> unlinked = standAloneEntitiesHandler.getUnlinkedEntities(bundle);
        standAloneEntitiesHandler.reattachLinkedStandAlones(unlinked, bundle);
        bundle.getIslands().removeAll(unlinked);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "REMOVED UNLINKED");

        mergeRelatedHandler.mergeRelatedIslands(bundle);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "MERGE RELATED");

        fullyIncludedRemoval.removeFullyIncluded(bundle);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "REMOVED FULLY INCLUDED");

        mergeRelatedHandler.mergeRelatedIslands(bundle);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "MERGED RELATED");
        
        standAloneIslandsHandler.handleStandAloneIslands(bundle);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "ONLY RELATED ISLANDS");
        printIslands(bundle.getStandAloneIslands(), "STAND ANLONE ISLANDS");
        
        mergeLowRangeHandler.mergeLowRankIslands(bundle);
        sortIslands(bundle);
        printIslands(bundle.getIslands(), "MERGED RELATED ISLANDS");

        logger.info("........................................");
        logger.info("                 MERGED ISLANDS");
        logger.info("........................................");
        logger.info(bundle.getMergedIslandsAsString());

        return bundle;
    }

    /**
     * Sort the islands in the current bundle by their rank (the biggest first).
     */
    public void sortIslands(EntityIslandBundle bundle) {
        List<EntityIsland> islands = new ArrayList<>(bundle.getIslands());
        islands.sort(new Comparator<EntityIsland>() {
            @Override
            public int compare(EntityIsland i1, EntityIsland i2) {
                if (i1.getIslandRank() == i2.getIslandRank()) {
                    return i2.getChildren().size() - i1.getChildren().size();
                }

                return i2.getIslandRank() - i1.getIslandRank();
            }
        });

        bundle.getIslands().clear();
        bundle.getIslands().addAll(islands);
    }

    private void printIslands(List<EntityIsland> islands, String stepName) {
        logger.info("----------------------------------------");
        logger.info("                 " + stepName);
        logger.info("----------------------------------------");
        logger.info(islands.toString());
    }
}
