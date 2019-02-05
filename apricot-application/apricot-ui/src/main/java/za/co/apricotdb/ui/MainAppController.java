package za.co.apricotdb.ui;

import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApricotProjectHandler;
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
    
    @Autowired
    ApricotProjectHandler projectHandler;
    
    @FXML
    BorderPane mainBorderPane;

    @FXML
    TabPane viewsTabPane;

    @FXML
    Button saveButton;

    private PropertyChangeListener canvasChangeListener;

    @FXML
    public void save(ActionEvent event) {
        for (Tab t : viewsTabPane.getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                //  save only changed canvas
                if (o.getCanvas().isCanvasChanged()) {
                    CanvasAllocationMap allocationMap = o.getCanvas().getAllocationMap();

                    ApricotView view = tabViewHandler.saveCanvasAllocationMap(allocationMap, o.getView());
                    o.setView(view);
                    
                    o.getCanvas().resetCanvasChange();
                }
            }
        }
        saveButton.setStyle("-fx-font-weight: normal;");
    }

    @FXML
    public void newView(ActionEvent event) throws Exception {
        viewHandler.createViewEditor(viewsTabPane, null, canvasChangeListener, null);
    }
    
    /**
     * Show a list of the projects, registered in the system.
     */
    @FXML
    public void openProject(ActionEvent event) {
        try {
            projectHandler.createOpenProjectForm(mainBorderPane, canvasChangeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Run the form of creation of the new project.
     */
    @FXML
    public void newProject(ActionEvent event) {
        try {
            projectHandler.createEditProjectForm(true, mainBorderPane, canvasChangeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Edit the currently selected project.
     */
    @FXML
    public void editProject(ActionEvent event) {
        try {
            projectHandler.createEditProjectForm(false, mainBorderPane, canvasChangeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PropertyChangeListener getCanvasChangeListener() {
        return canvasChangeListener;
    }

    public void setCanvasChangeListener(PropertyChangeListener canvasChangeListener) {
        this.canvasChangeListener = canvasChangeListener;
    }
}
