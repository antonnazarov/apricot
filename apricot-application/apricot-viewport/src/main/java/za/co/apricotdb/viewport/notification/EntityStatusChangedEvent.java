package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This event has been raised when any entity changed its status
 * (selected/desected).
 * 
 * @author Anton Nazarov
 * @since 02/10/2019
 */
public class EntityStatusChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2351742896269969857L;

    public EntityStatusChangedEvent(ApricotCanvas canvas) {
        super(canvas);
    }
}
