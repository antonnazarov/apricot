package javafxapplication.align;

import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import javafxapplication.canvas.ApricotBasicCanvas;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ElementType;

/**
 * Allocate the canvas for the minimal proper size.
 * 
 * @author Anton Nazarov
 * @since 14/12/2018
 */
public class CanvasSizeAjustor implements AlignCommand {

    public static final double DIAGRAM_PANEL_OFFSET = 10;

    private ApricotCanvas canvas = null;

    public CanvasSizeAjustor(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void align() {
        Bounds canvasBounds = getCanvasBounds();
        biasAllEntities(canvasBounds);
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
                
                VBox box = (VBox) e.getShape();

                double left = box.getLayoutX() + box.getTranslateX();
                double right = left + box.getWidth();
                double top = box.getLayoutY() + box.getTranslateY();
                double bottom = top + box.getHeight();

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
    private void biasAllEntities(Bounds canvasBounds) {
        Point2D bias = getEntitiesBias(canvasBounds);

        for (ApricotElement e : canvas.getElements()) {
            if (e.getElementType() == ElementType.ENTITY) {
                VBox box = (VBox) e.getShape();
                
                box.setLayoutX(box.getLayoutX() + bias.getX());
                box.setLayoutY(box.getLayoutY() + bias.getY());
            }
        }
    }
    
    private void alignCanvasSize(Bounds canvasBounds) {
        ApricotBasicCanvas boxCanvas = (ApricotBasicCanvas) canvas;
        if (canvasBounds.getRight() > boxCanvas.getPrefWidth()) {
            boxCanvas.setPrefWidth(canvasBounds.getRight() + DIAGRAM_PANEL_OFFSET);
        }
        if (canvasBounds.getBottom() > boxCanvas.getPrefHeight()) {
            boxCanvas.setPrefHeight(canvasBounds.getBottom() + DIAGRAM_PANEL_OFFSET);
        }
    }
}
