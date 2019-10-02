package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.EntityAlignHandler;

/**
 * The tool bar button: Align Top.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbAlignBottomHandler extends TbButtonHandlerState {

    @Autowired
    EntityAlignHandler alignHandler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    alignHandler.alignSelectedEntities(Side.BOTTOM);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbAlignBottomEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbAlignBottomDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Align selected Entities to the bottom <Ctrl+Down Arrow>";
    }
}
