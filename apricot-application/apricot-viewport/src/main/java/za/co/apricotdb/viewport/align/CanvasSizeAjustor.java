package za.co.apricotdb.viewport.align;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.event.GroupOperationHandler;

/**
 * Allocate the canvas for the minimal proper size.
 * 
 * @author Anton Nazarov
 * @since 14/12/2018
 */
public class CanvasSizeAjustor implements AlignCommand {

    public static final double DIAGRAM_PANEL_OFFSET = 10;

    private final GroupOperationHandler groupHandler = new GroupOperationHandler();
    private ApricotCanvas canvas = null;

    public CanvasSizeAjustor(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void align() {
        Bounds canvasBounds = getCanvasBounds();
        biasAllElements(canvasBounds);
        canvasBounds = getCanvasBounds();
        alignCanvasSize(canvasBounds);
    }

    /**
     * Get coordinates of the region, which surrounds all entities on the diagram.
     */
    private Bounds getCanvasBounds() {
        Bounds c = new Bounds(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);

        for (ApricotElement e : canvas.getElements()) {
            if (e.getElementType() == ElementType.ENTITY) {
                ApricotEntity entity = (ApricotEntity) e; 

                ApricotEntityShape entityShape = entity.getEntityShape();

                double left = entityShape.getLayoutX() + entityShape.getTranslateX();
                double right = left + entityShape.getWidth();
                double top = entityShape.getLayoutY() + entityShape.getTranslateY();
                double bottom = top + entityShape.getHeight();

                if (c.getLeft() > left) {
                    c.setLeft(left);
                }
                if (c.getRight() < right) {
                    c.setRight(right);
                }
                if (c.getTop() > top) {
                    c.setTop(top);
                }
                if (c.getBottom() < bottom) {
                    c.setBottom(bottom);
                }
            }
        }

        return c;
    }

    private Point2D getEntitiesBias(Bounds bounds) {

        double x = 0;
        double y = 0;
        if (bounds.getLeft() < DIAGRAM_PANEL_OFFSET) {
            x = DIAGRAM_PANEL_OFFSET - bounds.getLeft();
        }
        if (bounds.getTop() < DIAGRAM_PANEL_OFFSET) {
            y = DIAGRAM_PANEL_OFFSET - bounds.getTop();
        }

        Point2D ret = new Point2D(x, y);

        return ret;
    }

    /**
     * Re-position all entities to the calculated bias.
     */
    private void biasAllElements(Bounds canvasBounds) {
        Point2D bias = getEntitiesBias(canvasBounds);

        groupHandler.translateRelationshipRulers(canvas, bias.getX(), bias.getY(), null);
        groupHandler.resetRelationshipRulers(canvas);
        
        for (ApricotElement e : canvas.getElements()) {
            if (e.getShape() != null  && e.getElementType() == ElementType.ENTITY) {
                ApricotEntity entity = (ApricotEntity) e;
                ApricotEntityShape entityShape = entity.getEntityShape();
                entityShape.setLayoutX(entityShape.getLayoutX() + bias.getX());
                entityShape.setLayoutY(entityShape.getLayoutY() + bias.getY());
            }
        }
        
        canvas.buildRelationships();
    }

    private void alignCanvasSize(Bounds canvasBounds) {
        ApricotCanvasImpl boxCanvas = (ApricotCanvasImpl) canvas;
        boxCanvas.setPrefWidth(canvasBounds.getRight() + DIAGRAM_PANEL_OFFSET);
        boxCanvas.setPrefHeight(canvasBounds.getBottom() + DIAGRAM_PANEL_OFFSET);
    }
}
