package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.handler.CanvasContextMenuHandler;
import za.co.apricotdb.viewport.notification.CanvasContextMenuEvent;

/**
 * This listener serves the CanvasContextMenuEvent event.
 * 
 * @author Anton Nazarov
 * @since 06/05/2019
 */
@Component
public class CanvasContextMenuListener implements ApplicationListener<CanvasContextMenuEvent> {

    @Autowired
    CanvasContextMenuHandler menuHandler;

    @Override
    public void onApplicationEvent(CanvasContextMenuEvent event) {
        menuHandler.createCanvasContextMenu(event.getCanvas(), event.getX(), event.getY());
    }
}
