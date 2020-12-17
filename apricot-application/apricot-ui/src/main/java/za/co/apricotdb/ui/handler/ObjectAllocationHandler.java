package za.co.apricotdb.ui.handler;

import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private ApricotEntity currentScrolledEntity;

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
        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (!selected.isEmpty()) {
            Point2D corner = findMostTopLeft(selected);
            scrollToEntityCorner(tabInfo, corner);
        }
    }

    /**
     * Scroll to make visible the given entity.
     */
    public void scrollToEntity(TabInfoObject tabInfo, ApricotEntity entity) {
        Point2D corner = new Point2D(entity.getEntityShape().getLayoutX(), entity.getEntityShape().getLayoutY());
        scrollToEntityCorner(tabInfo, corner);
    }

    /**
     * Scroll to the next or previous selected entity.
     */
    public void scrollToNextEntity(TabInfoObject tabInfo, boolean forward) {
        ApricotCanvas canvas = tabInfo.getCanvas();
        List<ApricotEntity> selected = canvas.getSelectedEntities();
        if (selected.size() > 1) {
            List<ApricotEntity> sortedEntities = selected.stream()
                    .sorted(Comparator.comparing(o -> o.getEntityShape().getLayoutX())).collect(Collectors.toList());
            if (currentScrolledEntity == null) {
                currentScrolledEntity = sortedEntities.get(0);
            } else {
                if (forward) {
                    int idx = 0;
                    while (idx < sortedEntities.size()) {
                        if (sortedEntities.get(idx).equals(currentScrolledEntity)) {
                            if (idx == sortedEntities.size()-1) {
                                currentScrolledEntity = sortedEntities.get(0);
                            } else {
                                currentScrolledEntity = sortedEntities.get(idx+1);
                            }
                            break;
                        }
                        idx++;
                    }
                } else {
                    int idx = sortedEntities.size() - 1;
                    while (idx >= 0) {
                        if (sortedEntities.get(idx).equals(currentScrolledEntity)) {
                            if (idx == 0) {
                                currentScrolledEntity = sortedEntities.get(sortedEntities.size() - 1);
                            } else {
                                currentScrolledEntity = sortedEntities.get(idx-1);
                            }
                            break;
                        }
                        idx--;
                    }
                }
            }
        }

        if (currentScrolledEntity != null) {
            scrollToEntity(tabInfo, currentScrolledEntity);
        }
    }

    /**
     * Scroll to make visible the given entity.
     */
    public void scrollToEntityCorner(TabInfoObject tabInfo, Point2D corner) {
        ApricotCanvas canvas = tabInfo.getCanvas();
        Pane canvasBox = (Pane) canvas;
        ScrollPane scroll = tabInfo.getScroll();
        double deltaX = canvasBox.getWidth() - scroll.getWidth();
        double deltaY = canvasBox.getHeight() - scroll.getHeight();

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

    public void setCurrentScrolledEntity(ApricotEntity currentScrolledEntity) {
        this.currentScrolledEntity = currentScrolledEntity;
    }
}
