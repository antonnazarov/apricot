package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

/**
 * This Event has been raised when the Entity was moved (dragged).
 *
 * @author Anton Nazarov
 * @since 03/12/2020
 */
public class EntityMovedEvent extends ApplicationEvent {

    private ApricotEntityShape entityShape;
    private double translateX;
    private double translateY;
    private String entityName;

    public EntityMovedEvent(ApricotCanvas canvas, ApricotEntityShape entityShape, double translateX,
                            double translateY, String entityName) {
        super(canvas);

        this.entityShape = entityShape;
        this.translateX = translateX;
        this.translateY = translateY;
        this.entityName = entityName;
    }

    public EntityMovedEvent(ApricotCanvas canvas) {
        super(canvas);
    }

    public ApricotEntityShape getEntityShape() {
        return entityShape;
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public String getEntityName() {
        return entityName;
    }
}
