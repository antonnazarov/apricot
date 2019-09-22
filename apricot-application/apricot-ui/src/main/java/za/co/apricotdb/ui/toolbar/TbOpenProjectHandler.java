package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * The tool bar button: Open Project.
 *  
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbOpenProjectHandler implements TbButtonHandler {

    private Button button;

    @Override
    public void initButton(Button btn) {
        button = btn;
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
        return "tbOpenProjectEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbOpenProjectDisabled.png";
    }

    @Override
    public Button getButton() {
        return button;
    }
    
    @Override
    public String getToolpitText() {
        return "Open Project";
    }
}
