package za.co.apricotdb.viewport.canvas;

import za.co.apricotdb.viewport.event.CanvasOnMouseDraggedEventHandler;
import za.co.apricotdb.viewport.event.CanvasOnMousePressedEventHandler;
import za.co.apricotdb.viewport.event.CanvasOnMouseReleasedEventHandler;
import za.co.apricotdb.viewport.topology.TopologyManager;

public class ApricotCanvasBuilder implements CanvasBuilder {

    @Override
    public ApricotCanvas buildCanvas() {
        
        ApricotCanvasImpl canvas = new ApricotCanvasImpl();
        canvas.setOnMousePressed(new CanvasOnMousePressedEventHandler(new TopologyManager()));
        canvas.setOnMouseReleased(new CanvasOnMouseReleasedEventHandler());
        canvas.setOnMouseDragged(new CanvasOnMouseDraggedEventHandler());
        
        return canvas;
    }
}
