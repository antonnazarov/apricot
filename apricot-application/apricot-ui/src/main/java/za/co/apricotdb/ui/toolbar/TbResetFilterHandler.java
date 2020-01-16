package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.scene.control.Button;

/**
 * The filter button: Reset Filter.
 * 
 * @author Anton Nazarov
 * @since 16/01/2020
 */
@Component
public class TbResetFilterHandler extends TbButtonHandlerState {

    @Override
    public void initButton(Button btn) {
        init(btn);
    }

    @Override
    public String getEnabledImageName() {
        return "tbFilterReset.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbFilterReset.png";
    }

    @Override
    public String getToolpitText() {
        return "Reset the current filter";
    }
}
