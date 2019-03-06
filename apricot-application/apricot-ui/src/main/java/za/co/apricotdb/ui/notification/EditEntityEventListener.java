package za.co.apricotdb.ui.notification;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.handler.ApricotEntityHandler;
import za.co.apricotdb.viewport.notification.EditEntityEvent;

@Component
public class EditEntityEventListener implements ApplicationListener<EditEntityEvent> {
    
    @Autowired
    ApricotEntityHandler entityHandler;

    @Override
    public void onApplicationEvent(EditEntityEvent event) {
        try {
            entityHandler.openEntityEditorForm(false, event.getTableName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
