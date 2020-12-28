package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;

/**
 * This event has been raised when any entity changed its status
 * (selected/deselected).
 * 
 * @author Anton Nazarov
 * @since 02/10/2019
 */
public class EntityStatusChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2351742896269969857L;

    private String entityName;
    private ElementStatus status;

    public EntityStatusChangedEvent(ApricotCanvas canvas, String entityName, ElementStatus status) {
        super(canvas);

        this.entityName = entityName;
        this.status = status;
    }

    public EntityStatusChangedEvent(ApricotCanvas canvas) {
        super(canvas);
    }

    public String getEntityName() {
        return entityName;
    }

    public ElementStatus getStatus() {
        return status;
    }
}
