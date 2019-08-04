package za.co.apricotdb.viewport.canvas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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
        
        buildBorder(p);

        return canvas;
    }
    
    private void buildBorder(Pane p) {
        BorderStroke bs = new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.DASHED, new CornerRadii(0),
                new BorderWidths(2));
        Border border = new Border(bs);
        p.setBorder(border);
    }
}
