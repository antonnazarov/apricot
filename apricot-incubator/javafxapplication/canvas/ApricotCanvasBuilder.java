package javafxapplication.canvas;

import javafxapplication.event.CanvasOnMouseDraggedEventHandler;
import javafxapplication.event.CanvasOnMousePressedEventHandler;
import javafxapplication.event.CanvasOnMouseReleasedEventHandler;

public class ApricotCanvasBuilder implements CanvasBuilder {

    @Override
    public ApricotCanvas buildCanvas() {
        
        ApricotCanvasImpl canvas = new ApricotCanvasImpl();
        canvas.setOnMousePressed(new CanvasOnMousePressedEventHandler());
        canvas.setOnMouseReleased(new CanvasOnMouseReleasedEventHandler());
        canvas.setOnMouseDragged(new CanvasOnMouseDraggedEventHandler());
        
        return canvas;
    }
}
