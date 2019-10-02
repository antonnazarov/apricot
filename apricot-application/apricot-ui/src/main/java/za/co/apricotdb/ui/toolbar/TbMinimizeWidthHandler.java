package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.EntityAlignHandler;

/**
 * The tool bar button: Minimize Width.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbMinimizeWidthHandler extends TbButtonHandlerState {

    @Autowired
    EntityAlignHandler alignHandler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    alignHandler.alignEntitySize(true);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbMininizeWidthEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbMininizeWidthDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Minimize width of the selected Entities";
    }
}
