package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.viewport.align.island.CoreRelationshipsAllocator;
import za.co.apricotdb.viewport.align.island.EntityIsland;
import za.co.apricotdb.viewport.align.island.EntityIslandBundle;
import za.co.apricotdb.viewport.align.island.IslandAllocationHandler;
import za.co.apricotdb.viewport.align.island.IslandBundleHandler;
import za.co.apricotdb.viewport.align.island.IslandDistributionHandler;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;

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

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    IslandBundleHandler islandBundleHandler;

    @Autowired
    MainAppController appController;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    CoreRelationshipsAllocator relationshipsAllocator;

    @Autowired
    ResetViewHandler resetViewHandler;

    @Autowired
    CanvasScaleHandler scaleHandler;

    @ApricotErrorLogger(title = "Unable to align the Entities on the canvas")
    public void alignCanvasIslands() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        resetViewHandler.resetView(false);
        alignIslands(canvas);
        scaleHandler.fitCanvasScale();
    }

    private void alignIslands(ApricotCanvas canvas) {
        EntityIslandBundle islandBundle = islandBundleHandler.createIslandBundle(canvas);
        for (EntityIsland island : islandBundle.getAllIslands()) {
            allocationHandler.allocateIsland(island);
        }
        distributionHandler.distributeIslands(islandBundle);
        canvas.buildRelationships();
        relationshipsAllocator.allocateCoreRelationships(islandBundle);
        canvas.buildRelationships();
        saveAlignment(canvas);
    }

    private void saveAlignment(ApricotCanvas canvas) {
        CanvasChangedEvent canvasChangedEvent = new CanvasChangedEvent(canvas);
        eventPublisher.publishEvent(canvasChangedEvent);
        appController.save(null);
        snapshotHandler.synchronizeSnapshot(false);
    }
}
