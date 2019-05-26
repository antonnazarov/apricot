package za.co.apricotdb.viewport.canvas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

@Component
public class ApricotCanvasBuilder implements CanvasBuilder {

    @Autowired
    @Qualifier("CanvasOnMousePressedEventHandler")
    private EventHandler<MouseEvent> canvasOnMousePressedEventHandler;

    @Autowired
    @Qualifier("CanvasOnMouseReleasedEventHandler")
    private EventHandler<MouseEvent> canvasOnMouseReleasedEventHandler;

    @Autowired
    @Qualifier("CanvasOnMouseDraggedEventHandler")
    private EventHandler<MouseEvent> canvasOnMouseDraggedEventHandler;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public ApricotCanvas buildCanvas(String detailLevel, String erdNotation) {
        ApricotCanvas canvas = new ApricotCanvasImpl(applicationEventPublisher, detailLevel, erdNotation);
        Pane p = (Pane) canvas;
        p.setOnMousePressed(canvasOnMousePressedEventHandler);
        p.setOnMouseReleased(canvasOnMouseReleasedEventHandler);
        p.setOnMouseDragged(canvasOnMouseDraggedEventHandler);

        return canvas;
    }
}
