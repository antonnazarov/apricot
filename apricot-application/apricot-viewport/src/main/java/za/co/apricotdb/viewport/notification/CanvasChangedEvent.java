package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

/**
 * This event has been sent to UI when canvas was changed in the ViewPort.
 *  
 * @author Anton Nazarov
 * @since 06/03/2019
 */
public class CanvasChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2727573049836077994L;

    public CanvasChangedEvent(Object source) {
        super(source);
    }
}
