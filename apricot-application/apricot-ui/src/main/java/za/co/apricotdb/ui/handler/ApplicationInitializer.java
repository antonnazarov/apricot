package za.co.apricotdb.ui.handler;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.animation.PauseTransition;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.util.Duration;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.undo.ApricotUndoManager;
import za.co.apricotdb.viewport.canvas.CanvasBuilder;

/**
 * This controller is responsible for initialization of the application on
 * startup.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
@Component
public class ApplicationInitializer {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    TabViewHandler tabViewHandler;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    ViewManager viewManager;
    
    @Autowired
    CanvasBuilder canvasBuilder;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ApricotUndoManager undoManager;

    @Transactional
    public void initializeDefault() {
        ApricotProject currentProject = projectManager.findCurrentProject();
        if (currentProject == null) {
            // there is no project in the system
            initializeEmptyEnvironment();
            parentWindow.setEmptyEnv(true);
            return;
        } else {
            parentWindow.setEmptyEnv(false);
        }

        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(currentProject);
        if (defaultSnapshot != null) {
            initialize(currentProject, defaultSnapshot);
        }
    }

    @Transactional
    public void initializeForProject(ApricotProject project) {
        parentWindow.setEmptyEnv(false);
        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(project);
        initialize(project, defaultSnapshot);
    }

    @Transactional
    public void initialize(ApricotProject project, ApricotSnapshot snapshot) {
        // remember the current project
        parentWindow.getApplicationData().setCurrentProject(project);

        treeViewHandler.populate(project, snapshot);

        ComboBox<String> combo = parentWindow.getSnapshotCombo();
        if (combo.getUserData() != null && combo.getUserData().equals("snapshotCombo.selectSnapshot")) {
            combo.setUserData("reset");
        } else {
            combo.setUserData("AppInitialize");
            initCombo(project, snapshot, combo);
        }

        ApricotView currentView = viewManager.getCurrentView(project);
        Tab currentTab = null;
        TabPane tabPane = parentWindow.getProjectTabPane();
        tabPane.getTabs().clear();
        for (ApricotView view : viewHandler.getAllViews(project)) {
            // create Tabs for General view and for other views with the Layout Objects
            if (view.isGeneral() || view.getObjectLayouts().size() != 0) {
                Tab tb = viewHandler.createViewTab(snapshot, view, tabPane);
                if (view.equals(currentView)) {
                    currentTab = tb;
                }
            }
        }
        if (currentTab != null) {
            tabPane.getSelectionModel().select(currentTab);
        }

        // initialize the undo stack
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(e -> undoManager.resetUndoBuffer());
        delay.play();
    }

    private void initCombo(ApricotProject project, ApricotSnapshot snapshot, ComboBox<String> combo) {
        combo.getItems().clear();
        List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
        for (ApricotSnapshot s : snapshots) {
            combo.getItems().add(s.getName());
        }
        combo.setValue(snapshot.getName());
    }

    /**
     * Do it, when no projects were registered in the system.
     */
    private void initializeEmptyEnvironment() {
        TreeItem<String> root = new TreeItem<>("<No current Project>");
        parentWindow.getProjectTreeView().setRoot(root);
        ComboBox<String> combo = parentWindow.getSnapshotCombo();
        combo.getItems().clear();
        combo.setValue("<No current Snapshot>");
        TabPane tabPane = parentWindow.getProjectTabPane();
        tabPane.getTabs().clear();
    }
}
