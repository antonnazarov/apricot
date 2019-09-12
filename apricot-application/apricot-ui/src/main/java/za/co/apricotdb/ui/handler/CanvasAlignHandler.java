package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.ui.MainAppController;
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
    TabViewHandler tabViewHandler;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    IslandBundleHandler islandBundleHandler;

    @Autowired
    ObjectLayoutManager layoutManager;

    @Autowired
    MainAppController appController;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    CoreRelationshipsAllocator relationshipsAllocator;

    @Autowired
    ResetViewHandler resetViewHandler;

    public void alignCanvasIslands() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        resetViewHandler.resetView();

        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(e -> {
            alignIslands(canvas);
        });
        delay.play();

    }

    private void alignIslands(ApricotCanvas canvas) {
        EntityIslandBundle islandBundle = islandBundleHandler.createIslandBundle(canvas);
        for (EntityIsland island : islandBundle.getAllIslands()) {
            allocationHandler.allocateIsland(island);
        }
        distributionHandler.distributeIslands(islandBundle);
        canvas.buildRelationships();
        saveAlignment(canvas);

        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(e -> {
            relationshipsAllocator.allocateCoreRelationships(islandBundle);
            canvas.buildRelationships();
            saveAlignment(canvas);
        });
        delay.play();
    }

    private void saveAlignment(ApricotCanvas canvas) {
        CanvasChangedEvent canvasChangedEvent = new CanvasChangedEvent(canvas);
        eventPublisher.publishEvent(canvasChangedEvent);
        appController.save(null);
        snapshotHandler.syncronizeSnapshot(false);
    }
}
