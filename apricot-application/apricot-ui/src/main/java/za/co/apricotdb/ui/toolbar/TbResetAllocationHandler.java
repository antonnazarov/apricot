package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.ResetViewHandler;

/**
 * The tool bar button: Reset Allocation.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbResetAllocationHandler extends TbButtonHandlerState {

    @Autowired
    ResetViewHandler resetViewHandler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    resetViewHandler.resetView(true);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbResetAllocationEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbResetAllocationDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Reset the Diagram layout";
    }
}
