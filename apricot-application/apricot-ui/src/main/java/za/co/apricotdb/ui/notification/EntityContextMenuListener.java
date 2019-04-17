package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.handler.EntityContextMenuHandler;
import za.co.apricotdb.viewport.notification.EntityContextMenuEvent;

@Component
public class EntityContextMenuListener implements ApplicationListener<EntityContextMenuEvent> {

    @Autowired
    EntityContextMenuHandler menuHandler;

    @Override
    public void onApplicationEvent(EntityContextMenuEvent event) {
        menuHandler.createEntityContextMenu(event.getEntity(), event.getX(), event.getY());
    }
}
