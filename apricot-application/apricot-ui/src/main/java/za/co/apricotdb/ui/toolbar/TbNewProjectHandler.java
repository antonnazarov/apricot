package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ApricotProjectHandler;

/**
 * The tool bar button: New Project.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbNewProjectHandler extends TbButtonHandlerState {

    @Autowired
    ApricotProjectHandler projectHandler;

    @Autowired
    ParentWindow pw;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    try {
                        projectHandler.createEditProjectForm(true, pw.getMainAppPane());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbNewProjectEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbNewProjectDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "New Project";
    }
}
