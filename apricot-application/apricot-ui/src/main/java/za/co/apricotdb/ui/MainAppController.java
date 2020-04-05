package za.co.apricotdb.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ApricotAboutHandler;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.ApricotClipboardHandler;
import za.co.apricotdb.ui.handler.ApricotProjectHandler;
import za.co.apricotdb.ui.handler.ApricotRelationshipHandler;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.CanvasScaleHandler;
import za.co.apricotdb.ui.handler.CompareSnapshotsHandler;
import za.co.apricotdb.ui.handler.EntityAlignHandler;
import za.co.apricotdb.ui.handler.EntityFilterHandler;
import za.co.apricotdb.ui.handler.ExcelReportHandler;
import za.co.apricotdb.ui.handler.ExportProjectHandler;
import za.co.apricotdb.ui.handler.GenerateScriptHandler;
import za.co.apricotdb.ui.handler.ImportProjectHandler;
import za.co.apricotdb.ui.handler.NonTransactionalPort;
import za.co.apricotdb.ui.handler.ProjectExplorerContextMenuHandler;
import za.co.apricotdb.ui.handler.ProjectExplorerItem;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.ui.handler.SelectViewTabHandler;
import za.co.apricotdb.ui.handler.TabViewHandler;
import za.co.apricotdb.ui.toolbar.TbAddFilterHandler;
import za.co.apricotdb.ui.toolbar.TbButton;
import za.co.apricotdb.ui.toolbar.TbResetFilterHandler;
import za.co.apricotdb.ui.toolbar.TbSetFilterHandler;
import za.co.apricotdb.ui.toolbar.ToolbarHolder;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;

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
    ProjectExplorerContextMenuHandler explorerContextMenu;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ApricotAboutHandler aboutHandler;

    @Autowired
    CanvasScaleHandler scaleHandler;

    @Autowired
    ToolbarHolder tbHolder;

    @Autowired
    SelectViewTabHandler selTabHandler;

    @Autowired
    CompareSnapshotsHandler compareSnapshotsHandler;

    @Autowired
    ApricotClipboardHandler clipboardHandler;

    @Autowired
    EntityAlignHandler alignHandler;

    @Autowired
    EntityFilterHandler filterHandler;

    @Autowired
    TbSetFilterHandler tbSetFilterHandler;

    @Autowired
    TbAddFilterHandler tbAddFilterHandler;

    @Autowired
    TbResetFilterHandler tbResetFilterHandler;

    @Autowired
    ExportProjectHandler exportHandler;
    
    @Autowired
    ImportProjectHandler importHandler;

    @Autowired
    NonTransactionalPort port;
    
    @Autowired
    RepositoryHandler repositoryHandler;

    @FXML
    AnchorPane mainPane;

    @FXML
    TabPane viewsTabPane;

    @FXML
    ComboBox<String> snapshotCombo;

    @FXML
    TreeView<ProjectExplorerItem> projectsTreeView;

    @FXML
    ComboBox<String> scale;

    @FXML
    SplitPane splitPane;

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

    // filter
    @FXML
    Button tbSetFilter;
    @FXML
    Button tbAddFilter;
    @FXML
    Button tbResetFilter;

    // menu items
    @FXML
    MenuItem menuSave;
    @FXML
    MenuItem menuUndo;
    @FXML
    MenuItem menuCopy;
    @FXML
    MenuItem menuPaste;
    @FXML
    MenuItem menuLeft;
    @FXML
    MenuItem menuRight;
    @FXML
    MenuItem menuTop;
    @FXML
    MenuItem menuBottom;
    @FXML
    MenuItem menuMinWidth;
    @FXML
    MenuItem menuSameWidth;

    @FXML
    TextField filterField;

    public void init() {
        parentWindow.init(this);
        parentWindow.setParentPane(mainPane);
        parentWindow.setViewsTabPane(viewsTabPane);

        selTabHandler.initTabPane(viewsTabPane, scale);

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
                tbAllocateEntities, tbResetAllocation, tbExcelReport, tbInsertScript, tbDeleteScript, tbDropScript,
                tbReverseEngineering);

        filterField.setText("*");
        filterField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                filterAdd(null);
            }
        });

        tbSetFilterHandler.initButton(tbSetFilter);
        tbAddFilterHandler.initButton(tbAddFilter);
        tbResetFilterHandler.initButton(tbResetFilter);
    }

    public void save(ActionEvent event) {
        canvasHandler.saveEditedCanvases();

        tbHolder.disable(TbButton.tbSave);
        menuSave.setDisable(true);
        parentWindow.getApplicationData().setLayoutEdited(false);
    }

    public void newView(ActionEvent event) {
        try {
            viewHandler.createViewEditor(viewsTabPane, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newRelationship(ActionEvent event) {
        port.openRelationshipEditorForm(viewsTabPane);
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
                tbHolder.disable(TbButton.tbSave);
                parentWindow.getApplicationData().setLayoutEdited(false);
            }
        }

        if (snapshotCombo.getUserData() != null && snapshotCombo.getUserData().equals("AppInitialize")) {
            snapshotCombo.setUserData("reset");
        } else {
            snapshotCombo.setUserData("snapshotCombo.selectSnapshot");
            port.setDefaultSnapshot(snapshotCombo.getSelectionModel().getSelectedItem());
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

    @FXML
    public void compareSnapshots(ActionEvent event) {
        try {
            compareSnapshotsHandler.openCompareSnapshotsForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the current project.
     */
    @FXML
    public void deleteProject(ActionEvent event) {
        projectHandler.deleteProject();
    }

    /**
     * Start a process of creation of the new entity.
     */
    public void newEntity(ActionEvent event) {
        port.openEntityEditorForm(true, null);
    }

    @FXML
    public void createExcelReport(ActionEvent event) {
        port.createExcelReport(mainPane.getScene().getWindow());
    }
    
    @FXML
    public void repository(ActionEvent event) {
        repositoryHandler.showRepositoryForm();
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

    @FXML
    public void refresh(ActionEvent event) {
        snapshotHandler.syncronizeSnapshot(false);
    }

    @FXML
    public void copy(ActionEvent event) {
        clipboardHandler.copySelectedToClipboard();
        menuPaste.setDisable(false);
    }

    @FXML
    public void paste(ActionEvent event) {
        clipboardHandler.pasteSelectedFromClipboard();
    }

    @FXML
    public void selectAll(ActionEvent event) {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        if (canvas != null) {
            canvas.changeAllElementsStatus(ElementStatus.SELECTED, false);
        }
    }

    @FXML
    public void alignLeft(ActionEvent event) {
        alignHandler.alignSelectedEntities(Side.LEFT);
    }

    @FXML
    public void alignRight(ActionEvent event) {
        alignHandler.alignSelectedEntities(Side.RIGHT);
    }

    @FXML
    public void alignTop(ActionEvent event) {
        alignHandler.alignSelectedEntities(Side.TOP);
    }

    @FXML
    public void alignBottom(ActionEvent event) {
        alignHandler.alignSelectedEntities(Side.BOTTOM);
    }

    @FXML
    public void minWidth(ActionEvent event) {
        alignHandler.alignEntitySize(true);
    }

    @FXML
    public void sameWidth(ActionEvent event) {
        alignHandler.alignEntitySize(false);
    }

    @FXML
    public void exportProject(ActionEvent event) {
        exportHandler.exportProject(mainPane.getScene().getWindow());
    }

    @FXML
    public void importProject(ActionEvent event) {
        importHandler.importProject(mainPane.getScene().getWindow());
    }
    
    public TabPane getViewsTabPane() {
        return viewsTabPane;
    }

    public ComboBox<String> getScale() {
        return scale;
    }

    public MenuItem getMenuSave() {
        return menuSave;
    }

    public MenuItem getMenuUndo() {
        return menuUndo;
    }

    public MenuItem getMenuCopy() {
        return menuCopy;
    }

    public MenuItem getMenuLeft() {
        return menuLeft;
    }

    public MenuItem getMenuRight() {
        return menuRight;
    }

    public MenuItem getMenuTop() {
        return menuTop;
    }

    public MenuItem getMenuBottom() {
        return menuBottom;
    }

    public MenuItem getMenuMinWidth() {
        return menuMinWidth;
    }

    public MenuItem getMenuSameWidth() {
        return menuSameWidth;
    }

    @FXML
    public void filterOn(ActionEvent event) {
        filterHandler.setupEntityFilter(filterField.getText());
    }

    @FXML
    public void filterAdd(ActionEvent event) {
        filterHandler.addToEntityFilter(filterField.getText());
    }

    @FXML
    public void filterReset(ActionEvent event) {
        filterHandler.resetEntityFilter();
        filterField.setText("*");
    }

    public TextField getFilterField() {
        return filterField;
    }
}
