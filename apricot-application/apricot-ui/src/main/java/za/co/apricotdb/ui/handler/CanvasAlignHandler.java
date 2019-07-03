package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.viewport.align.EntityIsland;
import za.co.apricotdb.viewport.align.EntityIslandBundle;
import za.co.apricotdb.viewport.align.IslandAllocationHandler;
import za.co.apricotdb.viewport.align.IslandDistributionHandler;

/**
 * This handler calls the align operation on the current Canvas.
 * 
 * @author Anton Nazarov
 * @since 25/06/2019
 */
@Component
public class CanvasAlignHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    IslandAllocationHandler allocationHandler;
    
    @Autowired
    IslandDistributionHandler distributionHandler;

    public void alignCanvasIslands() {
        EntityIslandBundle islandBundle = new EntityIslandBundle(canvasHandler.getSelectedCanvas());
        islandBundle.initialize();

        for (EntityIsland island : islandBundle.getIslands()) {
            allocationHandler.allocateIsland(island);
        }
        
        distributionHandler.distributeIslands(islandBundle);
    }
}
