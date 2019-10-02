package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.toolbar.TbAlignBottomHandler;
import za.co.apricotdb.ui.toolbar.TbAlignLeftHandler;
import za.co.apricotdb.ui.toolbar.TbAlignRightHandler;
import za.co.apricotdb.ui.toolbar.TbAlignTopHandler;
import za.co.apricotdb.ui.toolbar.TbEditEntityHandler;
import za.co.apricotdb.ui.toolbar.TbSameWidthHandler;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.notification.EntityStatusChangedEvent;

/**
 * This listener handles the entity status change event.
 * 
 * @author Anton Nazarov
 * @since 02/10/2019
 */
@Component
public class EntityStatusChangedEventListener implements ApplicationListener<EntityStatusChangedEvent> {

    @Autowired
    TbEditEntityHandler editEntityHandler;
    @Autowired
    TbAlignTopHandler alignTopHandler;
    @Autowired
    TbAlignBottomHandler alignBottomHandler;
    @Autowired
    TbAlignLeftHandler alignLeftHandler;
    @Autowired
    TbAlignRightHandler alignRightHandler;
    @Autowired
    TbSameWidthHandler sameWidthHandler;

    @Override
    public void onApplicationEvent(EntityStatusChangedEvent event) {
        ApricotCanvas canvas = (ApricotCanvas) event.getSource();
        int selected = countSelectedEntities(canvas);
        handleSelection(selected);
    }

    private int countSelectedEntities(ApricotCanvas canvas) {
        return canvas.getSelectedEntities().size();
    }

    private void handleSelection(int selected) {
        if (selected == 0) {
            handleNone();
        } else if (selected == 1) {
            handleOne();
        } else {
            handleMany();
        }
    }

    private void handleOne() {
        editEntityHandler.enable();
        setAligners(false);
    }

    private void handleNone() {
        editEntityHandler.disable();
        setAligners(false);
    }

    private void handleMany() {
        editEntityHandler.disable();
        setAligners(true);
    }

    private void setAligners(boolean enabled) {
        if (enabled) {
            alignTopHandler.enable();
            alignBottomHandler.enable();
            alignLeftHandler.enable();
            alignRightHandler.enable();
            sameWidthHandler.enable();
        } else {
            alignTopHandler.disable();
            alignBottomHandler.disable();
            alignLeftHandler.disable();
            alignRightHandler.disable();
            sameWidthHandler.disable();
        }
    }
}
