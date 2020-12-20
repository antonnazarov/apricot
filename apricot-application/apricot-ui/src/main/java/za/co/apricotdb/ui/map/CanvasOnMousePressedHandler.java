package za.co.apricotdb.ui.map;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;

/**
 * The handler which handles the mouse clicked on the MapCanvas event.
 * The ActiveFrame moves to the clicked spot then.
 *
 * @author Anton Nazarov
 * @since 20/12/2020
 */
@Component
public class CanvasOnMousePressedHandler implements EventHandler<MouseEvent> {

    @Autowired
    MapHandler mapHandler;

    @Autowired
    ActiveFrameHandler activeFrameHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Override
    public void handle(MouseEvent event) {
        MapHolder holder = mapHandler.getMapHolder();

        Pane mapCanvas = holder.getMapCanvas();
        Pane activeFrame = holder.getActiveFrame();
        Point2D newPosition = getNewActiveFramePosition(event.getX(), event.getY(), mapCanvas, activeFrame);

        ScrollPane scroll =  canvasHandler.getCurrentViewTabInfo().getScroll();
        double hValue = activeFrameHandler.getScrollHvalue(mapCanvas, activeFrame, newPosition.getX());
        double vValue = activeFrameHandler.getScrollVvalue(mapCanvas, activeFrame, newPosition.getY());

        scroll.setHvalue(hValue);
        scroll.setVvalue(vValue);
    }

    Point2D getNewActiveFramePosition(double x, double y, Pane mapCanvas, Pane activeFrame) {
        double posX = x - activeFrame.getWidth()/2;
        double posY = y - activeFrame.getHeight()/2;

        if (x+activeFrame.getWidth() > mapCanvas.getWidth()) {
            posX = mapCanvas.getWidth() - activeFrame.getWidth();
            if (posX < 0) {
                posX = 0;
            }
        }

        if (y+activeFrame.getHeight() > mapCanvas.getHeight()) {
            posY = mapCanvas.getHeight() - activeFrame.getHeight();
            if (posY < 0) {
                posY = 0;
            }
        }

        return new Point2D(posX, posY);
    }
}
