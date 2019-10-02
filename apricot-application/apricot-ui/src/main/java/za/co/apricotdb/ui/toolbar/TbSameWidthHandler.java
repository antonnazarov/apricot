package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.EntityAlignHandler;

/**
 * The tool bar button: Make the same width.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbSameWidthHandler extends TbButtonHandlerState {

    @Autowired
    EntityAlignHandler alignHandler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    alignHandler.alignEntitySize(false);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbSameWidthEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbSameWidthDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Make the selected Entities the same width";
    }
}
