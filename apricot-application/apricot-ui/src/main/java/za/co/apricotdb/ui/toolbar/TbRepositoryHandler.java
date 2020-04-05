package za.co.apricotdb.ui.toolbar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import za.co.apricotdb.ui.MainAppController;

/**
 * The Apricot Repository button.
 * 
 * @author Anton Nazarov
 * @since 05/04/2020
 */
@Component
public class TbRepositoryHandler extends TbButtonHandlerState {

    @Autowired
    MainAppController appController;
    
    @Override
    public void initButton(Button btn) {
        init(btn);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isEnabled()) {
                    appController.repository(null);
                }
            }
        });
    }

    @Override
    public String getEnabledImageName() {
        return "tbApricotRepository.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbApricotRepository.png";
    }

    @Override
    public String getToolpitText() {
        return "Apricot Repository Import/Export";
    }
}
