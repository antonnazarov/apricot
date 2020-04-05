package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.QuickViewHandler;

/**
 * The Quick View button - creates a new QuickView.
 * 
 * @author Anton Nazarov
 * @since 05/04/2020
 */
@Component
public class TbQuickViewHandler extends TbButtonHandlerState {

    @Autowired
    QuickViewHandler quickViewHandler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    quickViewHandler.createQuickView();
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbQuickView.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbQuickView.png";
    }

    @Override
    public String getToolpitText() {
        return "Create Quick View including the selected Entities";
    }
}
