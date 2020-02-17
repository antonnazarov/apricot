package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.align.SimpleGridEntityAllocator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.notification.AddLayoutSavepointEvent;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * This handler resets the current view to the default grid alignment.
 * 
 * @author Anton Nazarov
 * @since 11/09/2019
 */
@Component
public class ResetViewHandler {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ObjectLayoutManager layoutManager;

    @Autowired
    MainAppController appController;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    CanvasScaleHandler scaleHandler;

    @ApricotErrorLogger(title = "Unable to reset the Entities allocation")
    public void resetView(boolean synchronize) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        ApricotView view = canvasHandler.getCurrentView();

        // prepare for undo
        AddLayoutSavepointEvent addSavepointEvent = new AddLayoutSavepointEvent(canvas);
        eventPublisher.publishEvent(addSavepointEvent);

        if (view.isGeneral()) {
            layoutManager.deleteViewObjectLayouts(view);
        }

        for (ApricotRelationship r : canvas.getRelationships()) {
            r.resetShape();
        }

        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                ApricotEntity entity = (ApricotEntity) elm;
                entity.getEntityShape().setPrefWidth(10);
            }
        }

        if (!view.isGeneral()) {
            AlignCommand aligner = new SimpleGridEntityAllocator(canvas);
            aligner.align();
            CanvasChangedEvent canvasChangedEvent = new CanvasChangedEvent(canvas);
            eventPublisher.publishEvent(canvasChangedEvent);
            appController.save(null);
        }

        if (synchronize) {
            snapshotHandler.syncronizeSnapshot(false);
            scaleHandler.fitCanvasScale();
        }
    }
}
