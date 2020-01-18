package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.scene.control.Button;

/**
 * The filter button: Set Filter.
 * 
 * @author Anton Nazarov
 * @since 08/01/2020
 */
@Component
public class TbSetFilterHandler extends TbButtonHandlerState {

    @Override
    public void initButton(Button btn) {
        init(btn);
    }

    @Override
    public String getEnabledImageName() {
        return "tbFilterSet.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbFilterSet.png";
    }

    @Override
    public String getToolpitText() {
        return "Set entities filter";
    }
}
