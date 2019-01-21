package za.co.apricotdb.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;

/**
 * This controller serves the main application form apricot-main.fxml.
 * 
 * @author Anton Nazarov
 * @since 20/01/2019
 */
@Component
public class MainApplicationWindowController {
    
    @Autowired
    ParentWindow parentWindow;
    
    @Autowired
    TabViewController tabViewController;
    
    @FXML
    public void save(ActionEvent event) {
        TabPane tabs = parentWindow.getProjectTabPane();
        for(Tab t : tabs.getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                CanvasAllocationMap allocationMap = o.getCanvas().getAllocationMap();
                
                tabViewController.saveCanvasAllocationMap(allocationMap, o.getView());
            }
        }
    }
}
