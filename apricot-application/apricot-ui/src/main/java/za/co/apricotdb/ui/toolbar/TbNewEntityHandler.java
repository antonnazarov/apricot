package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * The tool bar button: New Entity.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbNewEntityHandler extends TbButtonHandlerState {

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
        return "tbNewEntityEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbNewEntityDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "New Entity";
    }
}
