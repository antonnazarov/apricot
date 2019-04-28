package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

public class AddLayoutSavepointEvent extends ApplicationEvent {

    private static final long serialVersionUID = 3477478387072476564L;

    public AddLayoutSavepointEvent(Object source) {
        super(source);
    }
}
