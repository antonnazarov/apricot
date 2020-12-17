package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.handler.RelationshipContextMenuHandler;
import za.co.apricotdb.viewport.notification.RelationshipContextMenuEvent;

/**
 * The listener of the RelationshipContextMenuEvent.
 *
 * @author Anton Nazarov
 * @since 17/12/2020
 */
@Component
public class RelationshipContextMenuListener implements ApplicationListener<RelationshipContextMenuEvent> {

    @Autowired
    RelationshipContextMenuHandler relationshipContextMenuHandler;

    @Override
    public void onApplicationEvent(RelationshipContextMenuEvent event) {
        relationshipContextMenuHandler.createRelationshipContextMenu(event.getRelationship(), event.getX(), event.getY());
    }
}
