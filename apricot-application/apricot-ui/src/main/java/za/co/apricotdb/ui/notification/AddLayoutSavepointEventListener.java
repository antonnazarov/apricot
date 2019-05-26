package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.ui.undo.UndoType;
import za.co.apricotdb.viewport.notification.AddLayoutSavepointEvent;

/**
 * This listener handles the AddLayoutSavepointEvent to the undo stack.
 * 
 * @author Anton Nazarov
 * @since 26/04/2019
 */
@Component
public class AddLayoutSavepointEventListener implements ApplicationListener<AddLayoutSavepointEvent> {

    @Autowired
    ApricotUndoManager undoManager;

    @Override
    public void onApplicationEvent(AddLayoutSavepointEvent event) {
        undoManager.addSavepoint(UndoType.LAYOUT_CHANGED);
    }
}
