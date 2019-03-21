package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * This is a main entry point into the key press handling in UI.
 * 
 * @author Anton Nazarov
 * @since 20/03/2019
 */
@Component
public class OnKeyPressedEventHandler implements EventHandler<KeyEvent> {

    @Autowired
    DeleteSelectedHandler deleteSelectedHandler;

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
        case DELETE:
            deleteSelectedHandler.deleteSelected();
            break;
        default:
            break;
        }
    }
}
