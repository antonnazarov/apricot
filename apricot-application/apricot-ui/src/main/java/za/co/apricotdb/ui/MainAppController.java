package za.co.apricotdb.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ApricotEntityHandler;
import za.co.apricotdb.ui.handler.ApricotProjectHandler;
import za.co.apricotdb.ui.handler.ApricotRelationshipHandler;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
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

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    ApricotEntityHandler entityHandler;
    
    @Autowired
    ApricotRelationshipHandler relationshipHandler;

    @FXML
    BorderPane mainBorderPane;

    @FXML
    TabPane viewsTabPane;

    @FXML
    Button saveButton;

    @FXML
    ComboBox<String> snapshotCombo;

    @FXML
    public void save(ActionEvent event) {
        for (Tab t : viewsTabPane.getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                // save only changed canvas
                if (o.getCanvas().isCanvasChanged()) {
                    CanvasAllocationMap allocationMap = o.getCanvas().getAllocationMap();

                    ApricotView view = tabViewHandler.saveCanvasAllocationMap(allocationMap, o.getView());
                    o.setView(view);

                    t.setStyle("-fx-font-weight: normal;");
                    o.getCanvas().setCanvasChanged(false);
                }
            }
        }
        saveButton.setStyle("-fx-font-weight: normal;");
    }

    @FXML
    public void newView(ActionEvent event) throws Exception {
        viewHandler.createViewEditor(viewsTabPane, null, null);
    }
    
    @FXML
    public void newRelationship(ActionEvent event) throws Exception {
        relationshipHandler.openRelationshipEditorForm(viewsTabPane);
    }

    /**
     * Show a list of the projects, registered in the system.
     */
    @FXML
    public void openProject(ActionEvent event) {
        try {
            projectHandler.createOpenProjectForm(mainBorderPane);
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
            projectHandler.createEditProjectForm(true, mainBorderPane);
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
            projectHandler.createEditProjectForm(false, mainBorderPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new snapshot.
     */
    @FXML
    public void newSnapshot(ActionEvent event) {
        try {
            snapshotHandler.createEditSnapshotForm(true, mainBorderPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The snapshot was selected.
     */
    @FXML
    public void selectSnapshot(ActionEvent event) {
        if (snapshotCombo.getUserData() != null && snapshotCombo.getUserData().equals("AppInitialize")) {
            snapshotCombo.setUserData("reset");
        } else {
            snapshotCombo.setUserData("snapshotCombo.selectSnapshot");

            String snapshotSelected = snapshotCombo.getSelectionModel().getSelectedItem();
            ApricotSnapshot snapshot = snapshotManager
                    .getSnapshotByName(parentWindow.getApplicationData().getCurrentProject(), snapshotSelected);
            if (snapshot == null) {
                return;
            }
            snapshotHandler.setDefaultSnapshot(snapshot);
            applicationInitializer.initialize(snapshot.getProject(), snapshot);
        }
    }

    /**
     * Open the form of editing of the snapshot.
     */
    @FXML
    public void editSnapshot(ActionEvent event) {
        try {
            snapshotHandler.createEditSnapshotForm(false, mainBorderPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the chosen snapshot.
     */
    @FXML
    public void deleteSnapshot(ActionEvent event) {
        if (snapshotHandler.deleteSnapshot()) {
            applicationInitializer.initializeDefault();
        }
    }

    /**
     * Reverse engineer into the current project/snapshot.
     */
    @FXML
    public void reverseEngineer(ActionEvent event) {
        reverseEngineHandler.startReverseEngineering();
    }

    /**
     * Delete the current project.
     */
    @FXML
    public void deleteProject(ActionEvent event) {
        if (projectHandler.deleteCurrentProject()) {
            applicationInitializer.initializeDefault();
        }
    }

    /**
     * Start a process of creation of the new entity.
     */
    @FXML
    public void newEntity(ActionEvent event) {
        try {
            entityHandler.openEntityEditorForm(true, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TabPane getViewsTabPane() {
        return viewsTabPane;
    }

    public Button getSaveButton() {
        return saveButton;
    }
}
