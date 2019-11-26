package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * The tool bar button: Search.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbSearchHandler extends TbButtonHandlerState {

    @Override
    public void initButton(Button btn) {
        init(btn);
        disable();

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbSearchEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbSearchDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Search Entities";
    }
}
