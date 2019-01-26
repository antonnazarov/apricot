package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.ui.handler.TabViewHandler;
import za.co.apricotdb.viewport.canvas.CanvasAllocationMap;

/**
 * This controller serves the main application form apricot-main.fxml.
 * 
 * @author Anton Nazarov
 * @since 20/01/2019
 */
@Component
public class MainAppController {
    
    @Autowired
    TabViewHandler tabViewHandler;
    
    @Autowired
    ApricotViewHandler viewHandler;
    
    @FXML
    BorderPane mainBorderPane;
    
    @FXML
    TabPane viewsTabPane;
    
    @FXML
    Button saveButton;
    
    @FXML
    public void save(ActionEvent event) {
        for(Tab t : viewsTabPane.getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                CanvasAllocationMap allocationMap = o.getCanvas().getAllocationMap();
                
                tabViewHandler.saveCanvasAllocationMap(allocationMap, o.getView());
                
                o.getCanvas().resetCanvasChange();
            }
        }
    }
    
    @FXML
    public void newView(ActionEvent event) throws Exception {
        Stage primaryStage = (Stage) mainBorderPane.getScene().getWindow();
        viewHandler.createViewEditor(primaryStage, viewsTabPane, null);
    }
}
