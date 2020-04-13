package za.co.apricotdb.ui.notification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.handler.RelatedEntitiesHandler;
import za.co.apricotdb.viewport.notification.AbsentRelatedClickedEvent;

/**
 * This listener have been called on AbsentRelatedClickedEvent.
 * 
 * @author Anton Nazarov
 * @since 13/04/2020
 */
@Component
public class AbsentRelatedClickedListener implements ApplicationListener<AbsentRelatedClickedEvent> {

    @Autowired
    RelatedEntitiesHandler relatedEntitiesHandler;

    @Override
    public void onApplicationEvent(AbsentRelatedClickedEvent event) {
        List<String> req = new ArrayList<>();
        req.add(event.getTableName());
        relatedEntitiesHandler.makeRelatedEntitiesSelected(req);
    }
}
