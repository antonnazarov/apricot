package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

/**
 * The superclass for all ProgressBar related evants.
 *
 * @author Anton Nazarov
 * @since 14/05/2020
 */
public abstract class ProgressBarEvent extends ApplicationEvent {

    public ProgressBarEvent(Double progress) {
        super(progress);
    }
}
