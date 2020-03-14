package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.MainAppController;

/**
 * The tool bar button: Insert Script.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbCreateScriptHandler extends TbButtonHandlerState {

    @Autowired
    MainAppController appController;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    appController.generateCreateScript(null);
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
        return "Generate the \"CREATE\" script";
    }
}
