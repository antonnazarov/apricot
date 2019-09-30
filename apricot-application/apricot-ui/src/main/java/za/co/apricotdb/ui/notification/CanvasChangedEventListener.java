package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javafx.scene.control.Tab;
import za.co.apricotdb.ui.MainAppController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.toolbar.TbSaveHandler;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.notification.CanvasChangedEvent;

@Component
public class CanvasChangedEventListener implements ApplicationListener<CanvasChangedEvent> {

    @Autowired
    MainAppController appController;

    @Autowired
    ApricotUndoManager undoManager;
    
    @Autowired
    ParentWindow parent;
    
    @Autowired
    TbSaveHandler saveHandler;

    @Override
    public void onApplicationEvent(CanvasChangedEvent event) {
        Tab selectedTab = appController.getViewsTabPane().getSelectionModel().getSelectedItem();
        selectedTab.setStyle("-fx-font-weight: bold;");
        saveHandler.enable();

        ApricotCanvas canvas = (ApricotCanvas) event.getSource();
        canvas.setCanvasChanged(true);
        
        parent.getApplicationData().setLayoutEdited(true);
    }
}
