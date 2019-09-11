package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.viewport.align.island.CoreRelationshipsAllocator;
import za.co.apricotdb.viewport.align.island.EntityIsland;
import za.co.apricotdb.viewport.align.island.EntityIslandBundle;
import za.co.apricotdb.viewport.align.island.IslandAllocationHandler;
import za.co.apricotdb.viewport.align.island.IslandBundleHandler;
import za.co.apricotdb.viewport.align.island.IslandDistributionHandler;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.notification.AddLayoutSavepointEvent;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

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

    public void alignCanvasIslands() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();

        // prepare for undo
        AddLayoutSavepointEvent addSavepointEvent = new AddLayoutSavepointEvent(canvas);
        eventPublisher.publishEvent(addSavepointEvent);

        resetCurrentLayout(canvas);

        EntityIslandBundle islandBundle = islandBundleHandler.createIslandBundle(canvas);

        for (EntityIsland island : islandBundle.getAllIslands()) {
            allocationHandler.allocateIsland(island);
        }

        distributionHandler.distributeIslands(islandBundle);
        // canvas.buildRelationships();
        // relationshipsAllocator.allocateCoreRelationships(islandBundle);
        CanvasChangedEvent canvasChangedEvent = new CanvasChangedEvent(canvas);
        eventPublisher.publishEvent(canvasChangedEvent);
        canvas.buildRelationships();
        saveAlignment();

        // relationshipsAllocator.allocateCoreRelationships(islandBundle);
        // eventPublisher.publishEvent(canvasChangedEvent);
        // canvas.buildRelationships();
        // saveAlignment();
    }

    private void resetCurrentLayout(ApricotCanvas canvas) {
        layoutManager.deleteViewObjectLayouts(canvasHandler.getCurrentView());
        for (ApricotRelationship r : canvas.getRelationships()) {
            r.resetShape();
        }

        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                ApricotEntity entity = (ApricotEntity) elm;
                entity.getEntityShape().setPrefWidth(10);
            }
        }
    }

    private void saveAlignment() {
        appController.save(null);
        snapshotHandler.syncronizeSnapshot(false);
    }
}
