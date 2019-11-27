package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.toolbar.TbAlignBottomHandler;
import za.co.apricotdb.ui.toolbar.TbAlignLeftHandler;
import za.co.apricotdb.ui.toolbar.TbAlignRightHandler;
import za.co.apricotdb.ui.toolbar.TbAlignTopHandler;
import za.co.apricotdb.ui.toolbar.TbEditEntityHandler;
import za.co.apricotdb.ui.toolbar.TbMinimizeWidthHandler;
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
    @Autowired
    TbMinimizeWidthHandler minWidthHandler;
    @Autowired
    MainAppController appController;

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
        minWidthHandler.enable();
        appController.getMenuCopy().setDisable(false);
        appController.getMenuMinWidth().setDisable(false);
    }

    private void handleNone() {
        editEntityHandler.disable();
        setAligners(false);
        minWidthHandler.disable();
        appController.getMenuCopy().setDisable(true);
        appController.getMenuMinWidth().setDisable(true);
    }

    private void handleMany() {
        editEntityHandler.disable();
        setAligners(true);
        minWidthHandler.enable();
        appController.getMenuCopy().setDisable(false);
        appController.getMenuMinWidth().setDisable(false);
    }

    private void setAligners(boolean enabled) {
        if (enabled) {
            alignTopHandler.enable();
            alignBottomHandler.enable();
            alignLeftHandler.enable();
            alignRightHandler.enable();
            sameWidthHandler.enable();
            appController.getMenuLeft().setDisable(false);
            appController.getMenuRight().setDisable(false);
            appController.getMenuTop().setDisable(false);
            appController.getMenuBottom().setDisable(false);
            appController.getMenuSameWidth().setDisable(false);
        } else {
            alignTopHandler.disable();
            alignBottomHandler.disable();
            alignLeftHandler.disable();
            alignRightHandler.disable();
            sameWidthHandler.disable();
            appController.getMenuLeft().setDisable(true);
            appController.getMenuRight().setDisable(true);
            appController.getMenuTop().setDisable(true);
            appController.getMenuBottom().setDisable(true);
            appController.getMenuSameWidth().setDisable(true);
        }
    }
}
