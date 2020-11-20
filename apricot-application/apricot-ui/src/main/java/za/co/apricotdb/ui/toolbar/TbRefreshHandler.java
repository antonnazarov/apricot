package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;

/**
 * The tool bar button: Refresh.
 *  
 * @author Anton Nazarov
 * @since 21/09/2019
 */
@Component
public class TbRefreshHandler extends TbButtonHandlerState {

    @Autowired
    ApricotSnapshotHandler snapshotHandler;
    
    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    snapshotHandler.synchronizeSnapshot(false);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbRefreshEnabled.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbRefreshDisabled.png";
    }

    @Override
    public String getToolpitText() {
        return "Refresh <F5>";
    }
}
