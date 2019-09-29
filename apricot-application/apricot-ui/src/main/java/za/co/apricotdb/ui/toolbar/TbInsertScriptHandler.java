package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * The tool bar button: Insert Script.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbInsertScriptHandler extends TbButtonHandlerState {

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
        return "tbInsertScriptEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbInsertScriptDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Generate the \"INSERT\" script";
    }
}
