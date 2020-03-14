package za.co.apricotdb.ui.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.entity.ApricotEntity;
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

    private static final Double BIAS_RATIO = 0.2;

    @Autowired
    TabViewHandler tabViewHandler;

    /**
     * Center the Entity in the visible part of the screen.
     */
    public void centerEntityOnView(TabInfoObject tabInfo, String tableName, double biasX, double biasY) {
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
        
        layoutX += biasX;
        layoutY += biasY;

        CanvasAllocationItem alloc = ApricotEntityShape.getAllocation(tableName, layoutX, layoutY, 0);
        CanvasAllocationMap map = new CanvasAllocationMap();
        map.addCanvasAllocationItem(alloc);
        tabViewHandler.saveCanvasAllocationMap(map, tabInfo.getView());
    }

    /**
     * Scroll inside the scrolling area to make the selected entities maximally
     * visible.
     */
    public void scrollToSelected(TabInfoObject tabInfo) {
        ApricotCanvas canvas = tabInfo.getCanvas();
        Pane canvasBox = (Pane) canvas;
        ScrollPane scroll = tabInfo.getScroll();
        double deltaX = canvasBox.getWidth() - scroll.getWidth();
        double deltaY = canvasBox.getHeight() - scroll.getHeight();
        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (!selected.isEmpty()) {
            Point2D corner = findMostTopLeft(selected);
            if (deltaX > 0) {
                double hvalue = (corner.getX() - scroll.getWidth() * BIAS_RATIO) / deltaX;
                hvalue = fixScrollValue(hvalue);
                scroll.setHvalue(hvalue);
            }
            if (deltaY > 0) {
                double vvalue = (corner.getY() - scroll.getHeight() * BIAS_RATIO) / deltaY;
                vvalue = fixScrollValue(vvalue);
                scroll.setVvalue(vvalue);
            }
        }
    }

    private double fixScrollValue(double xvalue) {
        double ret = xvalue;
        if (xvalue < 0) {
            ret = 0;
        } else if (xvalue > 1) {
            ret = 1;
        }

        return ret;
    }

    private Point2D findMostTopLeft(List<ApricotEntity> entities) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;

        for (ApricotEntity ent : entities) {
            if (ent.getEntityShape().getLayoutX() < minX) {
                minX = ent.getEntityShape().getLayoutX();
                minY = ent.getEntityShape().getLayoutY();
            }
        }

        return new Point2D(minX, minY);
    }
}
