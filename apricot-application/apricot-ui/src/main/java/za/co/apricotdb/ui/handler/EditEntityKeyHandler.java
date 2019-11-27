package za.co.apricotdb.ui.handler;

import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import za.co.apricotdb.ui.EditEntityController;
import za.co.apricotdb.ui.EditEntityController.CurrentTab;

/**
 * Handler of all key presses in the Edit Entity form.
 * 
 * @author Anton Nazarov
 * @since 26/03/2019
 */
@Component
public class EditEntityKeyHandler implements EventHandler<KeyEvent> {

    private EditEntityController controller;

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
        //case ESCAPE:
        //    controller.cancel(null);
        //    event.consume();
        //    break;

        case ENTER:
            if (controller.getCurrentTab() == CurrentTab.CONSTRAINTS) {
                controller.editConstraint(null);
                event.consume();
            }
            break;

        case DELETE:
            if (controller.getCurrentTab() == CurrentTab.COLUMNS) {
                //  this does not work properly if editing the field
                //  controller.deleteColumn(null);
            } else {
                controller.deleteConstraint(null);
                event.consume();
            }
            break;

        case DOWN:
            if (controller.getCurrentTab() == CurrentTab.COLUMNS && controller.isLastColumn()) {
                controller.newColumn(null);
                event.consume();
            }
            break;

        default:
            break;
        }
    }

    public void setEditEntityController(EditEntityController controller) {
        this.controller = controller;
    }
}
