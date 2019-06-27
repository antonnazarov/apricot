package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

/**
 * This component contains the business logic useful for allocation of the
 * objects on Canvas.
 * 
 * @author Anton Nazarov
 * @since 27/05/2019
 */
@Component
public class ObjectAllocationHandler {

    @Autowired
    TabViewHandler tabViewHandler;

    /**
     * Center the Entity in the visible part of the screen.
     */
    public void centerEntityOnView(TabInfoObject tabInfo, String tableName) {
        ApricotCanvas canvas = tabInfo.getCanvas();
        double canvasWidth = ((Pane) canvas).getWidth();
        double canvasHeight = ((Pane) canvas).getHeight();
        ScrollPane scroll = tabInfo.getScroll();
        double visibleWidth = scroll.getWidth();
        double visibleHeight = scroll.getHeight();
        double horizontalBias = scroll.getHvalue();
        double verticalBias = scroll.getVvalue();

        double layoutX = 0;
        if (canvasWidth > visibleWidth) {
            layoutX = (canvasWidth - visibleWidth) * horizontalBias + visibleWidth / 2;
        } else {
            layoutX = visibleWidth / 2;
        }
        double layoutY = 0;
        if (canvasHeight > visibleHeight) {
            layoutY = (canvasHeight - visibleHeight) * verticalBias + visibleHeight / 2;
        } else {
            layoutY = visibleHeight / 2;
        }

        CanvasAllocationItem alloc = ApricotEntityShape.getAllocation(tableName, layoutX, layoutY, 0);
        CanvasAllocationMap map = new CanvasAllocationMap();
        map.addCanvasAllocationItem(alloc);
        tabViewHandler.saveCanvasAllocationMap(map, tabInfo.getView());
    }
}
