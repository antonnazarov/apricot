package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.map.MapHandler;
import za.co.apricotdb.viewport.notification.EntityMovedEvent;

/**
 * This listener handles the movement of the Entity.
 *
 * @author Anton Nazarov
 * @since 03/12/2020
 */
@Component
public class EntityMovedListener implements ApplicationListener<EntityMovedEvent> {

    @Autowired
    MapHandler mapHandler;

    @Override
    public void onApplicationEvent(EntityMovedEvent entityMovedEvent) {
        mapHandler.moveEntity(entityMovedEvent.getEntityShape(), entityMovedEvent.getTranslateX(),
                entityMovedEvent.getTranslateY(), entityMovedEvent.getEntityName());
    }
}
