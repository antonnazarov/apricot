package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.geometry.Side;
import javafx.util.Duration;
import za.co.apricotdb.viewport.align.EntityPositionAligner;
import za.co.apricotdb.viewport.align.EntitySizeAligner;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;

/**
 * This is a lightweight aligner of the selected entities.
 * 
 * @author Anton Nazarov
 * @since 20/04/2019
 */
@Component
public class EntityAlignHandler {

    @Autowired
    EntityPositionAligner positionAligner;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    EntityPositionAligner entityPositionAligner;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EntitySizeAligner sizeAligner;

    public void alignSelectedEntities(Side side) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        boolean edited = false;
        switch (side) {
        case LEFT:
            edited = entityPositionAligner.alignSelectedEntitiesLeft(canvas);
            break;
        case RIGHT:
            edited = entityPositionAligner.alignSelectedEntitiesRight(canvas);
            break;
        case TOP:
            edited = entityPositionAligner.alignSelectedEntitiesTop(canvas);
            break;
        case BOTTOM:
            edited = entityPositionAligner.alignSelectedEntitiesBottom(canvas);
            break;
        }

        if (edited) {
            setEditedAlert(canvas);
        }
    }

    public void alignEntitySize(boolean minimize) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        if (minimize) {
            sizeAligner.minimizeSelectedEntitiesWidth(canvas);
            setEditedAlert(canvas);
        } else {
            if (sizeAligner.alignEntitiesSameWidth(canvas)) {
                setEditedAlert(canvas);
            }
        }
    }

    private void setEditedAlert(ApricotCanvas canvas) {
        CanvasChangedEvent event = new CanvasChangedEvent(canvas);
        eventPublisher.publishEvent(event);
        PauseTransition delay = new PauseTransition(Duration.seconds(0.01));
        delay.setOnFinished(e -> canvas.buildRelationships());
        delay.play();
    }
}
