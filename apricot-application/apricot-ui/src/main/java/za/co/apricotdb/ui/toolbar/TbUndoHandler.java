package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * The tool bar button: Undo.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbUndoHandler extends TbButtonHandlerState {

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (btn.isDisabled()) {
                    enable();
                } else {
                    disable();
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
