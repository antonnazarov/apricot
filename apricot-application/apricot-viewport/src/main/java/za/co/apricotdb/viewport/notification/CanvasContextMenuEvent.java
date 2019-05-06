package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This event is sent when the right mouse button was clicked in the canvas.
 * 
 * @author Anton Nazarov
 * @since 06/05/2019
 */
public class CanvasContextMenuEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2196995597373175140L;

    private double x;
    private double y;
    private ApricotCanvas canvas;

    public CanvasContextMenuEvent(ApricotCanvas canvas, double x, double y) {
        super(canvas);
        this.x = x;
        this.y = y;
        this.canvas = canvas;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ApricotCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(ApricotCanvas canvas) {
        this.canvas = canvas;
    }
}
