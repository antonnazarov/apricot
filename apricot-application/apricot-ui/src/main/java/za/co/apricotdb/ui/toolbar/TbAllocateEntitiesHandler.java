package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.CanvasAlignHandler;

/**
 * The tool bar button: Allocate Entities.
 * 
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbAllocateEntitiesHandler extends TbButtonHandlerState {

    @Autowired
    CanvasAlignHandler aligner;

    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    aligner.alignCanvasIslands();
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbAllocateEntitiesEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbAllocateEntitiesDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Layout the Diagram nicely";
    }
}
