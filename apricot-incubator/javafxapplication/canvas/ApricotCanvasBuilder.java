package javafxapplication.canvas;

import javafxapplication.event.CanvasOnMouseDraggedEventHandler;
import javafxapplication.event.CanvasOnMousePressedEventHandler;
import javafxapplication.event.CanvasOnMouseReleasedEventHandler;

public class ApricotCanvasBuilder implements CanvasBuilder {

    @Override
    public ApricotCanvas buildCanvas() {
        
        ApricotBasicCanvas canvas = new ApricotBasicCanvas();
        canvas.setOnMousePressed(new CanvasOnMousePressedEventHandler());
        canvas.setOnMouseReleased(new CanvasOnMouseReleasedEventHandler());
        canvas.setOnMouseDragged(new CanvasOnMouseDraggedEventHandler());
        
        return canvas;
    }
}
