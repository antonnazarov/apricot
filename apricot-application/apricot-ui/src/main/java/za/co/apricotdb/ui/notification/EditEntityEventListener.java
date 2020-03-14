package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.handler.NonTransactionalPort;
import za.co.apricotdb.viewport.notification.EditEntityEvent;

@Component
public class EditEntityEventListener implements ApplicationListener<EditEntityEvent> {

    @Autowired
    NonTransactionalPort port;

    @Override
    public void onApplicationEvent(EditEntityEvent event) {
        port.openEntityEditorForm(false, event.getTableName());
    }
}
