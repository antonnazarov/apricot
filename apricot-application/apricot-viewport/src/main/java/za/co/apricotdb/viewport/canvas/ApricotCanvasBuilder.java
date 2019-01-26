package za.co.apricotdb.viewport.canvas;

import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    
    @Override
    public ApricotCanvas buildCanvas(PropertyChangeListener canvasChangeListener) {
        ApricotCanvas canvas = new ApricotCanvasImpl();
        canvas.addCanvasChangeListener(canvasChangeListener);
        Pane p = (Pane) canvas;
        p.setOnMousePressed(canvasOnMousePressedEventHandler);
        p.setOnMouseReleased(canvasOnMouseReleasedEventHandler);
        p.setOnMouseDragged(canvasOnMouseDraggedEventHandler);
        
        return canvas;
    }
}
