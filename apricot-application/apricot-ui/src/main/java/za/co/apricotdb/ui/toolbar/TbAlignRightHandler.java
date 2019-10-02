package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.EntityAlignHandler;

/**
 * The tool bar button: Align Right.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbAlignRightHandler extends TbButtonHandlerState {

    @Autowired
    EntityAlignHandler alignHandler;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    alignHandler.alignSelectedEntities(Side.RIGHT);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbAlignRightEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbAlignRightDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Align selected Entities to the right <Ctrl+Right Arrow>";
    }
}
