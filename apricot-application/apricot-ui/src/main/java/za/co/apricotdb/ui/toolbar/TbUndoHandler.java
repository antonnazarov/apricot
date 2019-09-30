package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.MainAppController;

/**
 * The tool bar button: Undo.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbUndoHandler extends TbButtonHandlerState {

    @Autowired
    MainAppController appController;

    @Override
    public void initButton(Button btn) {
        init(btn);
        disable();

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    appController.undo(null);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbUndoEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbUndoDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Undo <Ctrl+Z>";
    }
}
