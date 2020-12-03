package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

/**
 * This Event has been raised when the Apricot Map needs to be redrawn.
 *
 * @author Anton Nazarov
 * @since 03/12/2020
 */
public class RedrawMapEvent extends ApplicationEvent {

    public RedrawMapEvent(Object source) {
        super(source);
    }
}
