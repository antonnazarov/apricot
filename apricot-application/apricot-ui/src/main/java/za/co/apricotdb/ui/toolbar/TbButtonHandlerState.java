package za.co.apricotdb.ui.toolbar;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import za.co.apricotdb.ui.util.TooltipHelper;

/**
 * The holder of the button state.
 * 
 * @author Anton Nazarov
 * @since 29/09/2019
 */
public abstract class TbButtonHandlerState implements TbButtonHandler {

    private boolean enabled;
    private Button button;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable() {
        button.setDisable(false);
        setImage(button, getEnabledImageName());
        enabled = true;
    }

    @Override
    public void disable() {
        setImage(button, getDisabledImageName());
        enabled = false;
    }

    @Override
    public Button getButton() {
        return button;
    }

    @Override
    public void init(Button btn) {
        button = btn;
        btn.setText(null);
        enable();

        btn.setTooltip(TooltipHelper.getApricotTooltip(getToolpitText()));

        button.addEventFilter(KeyEvent.ANY, Event::consume);
    }
}
