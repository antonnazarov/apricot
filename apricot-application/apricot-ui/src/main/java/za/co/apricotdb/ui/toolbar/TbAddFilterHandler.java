package za.co.apricotdb.ui.toolbar;

import org.springframework.stereotype.Component;

import javafx.scene.control.Button;

/**
 * The filter button: Add Filter.
 * 
 * @author Anton Nazarov
 * @since 08/01/2020
 */
@Component
public class TbAddFilterHandler extends TbButtonHandlerState {

    @Override
    public void initButton(Button btn) {
        init(btn);
    }

    @Override
    public String getEnabledImageName() {
        return "tbFilterAdd.png";
    }

    @Override
    public String getDisabledImageName() {
        return "tbFilterAdd.png";
    }

    @Override
    public String getToolpitText() {
        return "Add entities to the current filter";
    }
}
