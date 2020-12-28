package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.map.MapHandler;
import za.co.apricotdb.viewport.notification.RedrawMapEvent;

/**
 * This listener calls redraw of the Apricot Map
 */
@Component
public class RedrawMapEventListener implements ApplicationListener<RedrawMapEvent> {

    @Autowired
    MapHandler mapHandler;

    @Override
    public void onApplicationEvent(RedrawMapEvent redrawMapEvent) {
        mapHandler.drawMap();
    }
}
