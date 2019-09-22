package za.co.apricotdb.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ApricotAboutHandler;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.ApricotEntityHandler;
import za.co.apricotdb.ui.handler.ApricotProjectHandler;
import za.co.apricotdb.ui.handler.ApricotRelationshipHandler;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.CanvasScaleHandler;
import za.co.apricotdb.ui.handler.ExcelReportHandler;
import za.co.apricotdb.ui.handler.GenerateScriptHandler;
import za.co.apricotdb.ui.handler.ProjectExplorerContextMenuHandler;
import za.co.apricotdb.ui.handler.ProjectExplorerItem;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.ui.handler.TabViewHandler;
import za.co.apricotdb.ui.handler.TreeViewHandler;
import za.co.apricotdb.ui.toolbar.ToolbarHolder;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

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
    ViewManager viewManager;

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

    @Autowired
    ExcelReportHandler excelReportHandler;

    @Autowired
    GenerateScriptHandler generateScriptHandler;

    @Autowired
    ApricotUndoManager undoManager;

    @Autowired
    AlertMessageDecorator alert;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ProjectExplorerContextMenuHandler explorerContextMenu;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ApricotAboutHandler aboutHandler;

    @Autowired
    CanvasScaleHandler scaleHandler;

    @Autowired
    ToolbarHolder tbHolder;

    @FXML
    AnchorPane mainPane;

    @FXML
    TabPane viewsTabPane;

    @FXML
    Button saveButton;

    @FXML
    Button undoButton;

    @FXML
    ComboBox<String> snapshotCombo;

    @FXML
    TreeView<ProjectExplorerItem> projectsTreeView;

    @FXML
    ComboBox<String> scale;

    // tool bar
    @FXML
    Button tbNewProject;
    @FXML
    Button tbOpenProject;
    @FXML
    Button tbEditProject;
    @FXML
    Button tbSave;
    @FXML
    Button tbUndo;
    @FXML
    Button tbRefresh;
    @FXML
    Button tbNewSnapshot;
    @FXML
    Button tbEditSnapshot;
    @FXML
    Button tbCompareSnapshot;
    @FXML
    Button tbNewView;
    @FXML
    Button tbEditView;
    @FXML
    Button tbNewEntity;
    @FXML
    Button tbEditEntity;
    @FXML
    Button tbNewRelationship;
    @FXML
    Button tbSearch;
    @FXML
    Button tbAlignLeft;
    @FXML
    Button tbAlignRight;
    @FXML
    Button tbAlignTop;
    @FXML
    Button tbAlignBottom;
    @FXML
    Button tbSameWidth;
    @FXML
    Button tbMinimizeWidth;
    @FXML
    Button tbAllocateEntities;
    @FXML
    Button tbResetAllocation;
    @FXML
    Button tbExcelReport;
    @FXML
    Button tbInsertScript;
    @FXML
    Button tbDeleteScript;
    @FXML
    Button tbDropScript;
    @FXML
    Button tbReverseEngineering;

    public void init() {
        parentWindow.setParentPane(mainPane);

        viewsTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                if (t1 != null) {
                    TabInfoObject tabInfo = TabInfoObject.getTabInfo(t1);
                    viewManager.setCurrentView(tabInfo.getView());
                    treeViewHandler.markEntitiesIncludedIntoView(tabInfo.getView());
                    scaleHandler.resetScaleIndicator(scale);
                }

                // reset the current undo layout
                PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
                delay.setOnFinished(e -> undoManager.resetCurrentLayout());
                delay.play();
            }
        });

        projectsTreeView.setOnContextMenuRequested(e -> {
            ApricotView view = canvasHandler.getCurrentView();
            if (view != null) {
                boolean isMainView = view.getName().equals(ApricotView.MAIN_VIEW);
                ContextMenu m = explorerContextMenu.buildContextMenu(projectsTreeView, isMainView);
                if (m != null) {
                    m.setAutoHide(true);
                    m.show(parentWindow.getWindow(), e.getScreenX(), e.getSceneY());
                }
            }
        });

        projectsTreeView.setOnKeyPressed(e -> {
            Pane canvas = (Pane) canvasHandler.getSelectedCanvas();
            canvas.fireEvent(e);
            canvas.requestFocus();
        });

        scale.getItems().clear();
        scale.getItems().add("100%");
        scale.getItems().add("80%");
        scale.getItems().add("50%");
        scale.getItems().add("20%");
        scale.getItems().add("10%");
        scale.getSelectionModel().select("100%");

        // initialize toolbar
        tbHolder.init(tbNewProject, tbOpenProject, tbEditProject, tbSave, tbUndo, tbRefresh, tbNewSnapshot,
                tbEditSnapshot, tbCompareSnapshot, tbNewView, tbEditView, tbNewEntity, tbEditEntity, tbNewRelationship,
                tbSearch, tbAlignLeft, tbAlignRight, tbAlignTop, tbAlignBottom, tbSameWidth, tbMinimizeWidth,
                tbAllocateEntities, tbResetAllocation, tbExcelReport, tbInsertScript, tbDeleteScript, tbDropScript, tbReverseEngineering);
    }

    @FXML
    public void save(ActionEvent event) {
        for (Tab t : viewsTabPane.getTabs()) {
            if (t.getUserData() instanceof TabInfoObject) {
                TabInfoObject o = (TabInfoObject) t.getUserData();
                // save only changed canvas
                if (o.getCanvas().isCanvasChanged()) {
                    tabViewHandler.saveCanvasAllocationMap(o);
                    t.setStyle("-fx-font-weight: normal;");
                    o.getCanvas().setCanvasChanged(false);
                }
            }
        }
        saveButton.setStyle("-fx-font-weight: normal;");

        parentWindow.getApplicationData().setLayoutEdited(false);
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
            projectHandler.createOpenProjectForm(mainPane);
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
            projectHandler.createEditProjectForm(true, mainPane);
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
            projectHandler.createEditProjectForm(false, mainPane);
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
            snapshotHandler.createEditSnapshotForm(true, mainPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The snapshot was selected.
     */
    @FXML
    public void selectSnapshot(ActionEvent event) {
        // check if there are some changes made
        if (parentWindow.getApplicationData().isLayoutEdited()) {
            if (alert.requestYesNoOption("Save changes",
                    "There are not saved changed made on the current diagram. Do you want to save them", "Save")) {
                save(event);
            } else {
                saveButton.setStyle("-fx-font-weight: normal;");
                parentWindow.getApplicationData().setLayoutEdited(false);
            }
        }

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
            snapshotManager.setDefaultSnapshot(snapshot);
            applicationInitializer.initialize(snapshot.getProject(), snapshot);
        }
    }

    /**
     * Open the form of editing of the snapshot.
     */
    @FXML
    public void editSnapshot(ActionEvent event) {
        try {
            snapshotHandler.createEditSnapshotForm(false, mainPane);
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

    @FXML
    public void createExcelReport(ActionEvent event) {
        excelReportHandler.createExcelReport(mainPane.getScene().getWindow());
    }

    @FXML
    public void generateCreateScript(ActionEvent event) {
        try {
            generateScriptHandler.createGenerateScriptForm(DBScriptType.CREATE_SCRIPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateDropScript(ActionEvent event) {
        try {
            generateScriptHandler.createGenerateScriptForm(DBScriptType.DROP_SCRIPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateDeleteScript(ActionEvent event) {
        try {
            generateScriptHandler.createGenerateScriptForm(DBScriptType.DELETE_SCRIPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void undo(ActionEvent event) {
        undoManager.undo();
    }

    @FXML
    public void about(ActionEvent event) {
        try {
            aboutHandler.showAboutForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void usersGuide(ActionEvent event) {
        Application app = parentWindow.getApplication();
        app.getHostServices().showDocument("https://www.apricotdb.co.za/joomla/index.php/user-guide");
    }

    @FXML
    public void setScale(ActionEvent event) {
        scaleHandler.setScale(scale.getSelectionModel().getSelectedItem());
    }

    public TabPane getViewsTabPane() {
        return viewsTabPane;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getUndoButton() {
        return undoButton;
    }

    public ComboBox<String> getScale() {
        return scale;
    }
}
