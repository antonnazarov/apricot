package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.AdvancedSearchHandler;

/**
 * The tool bar button: Search.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbSearchHandler extends TbButtonHandlerState {

    @Autowired
    AdvancedSearchHandler handler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handler.openSearchForm();
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
