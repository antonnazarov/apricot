package za.co.apricotdb.ui.handler;

import javafx.animation.PauseTransition;
import javafx.concurrent.Worker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ProjectExplorerItem.ItemType;
import za.co.apricotdb.ui.log.ApricotInfoLogger;
import za.co.apricotdb.ui.map.MapHandler;
import za.co.apricotdb.ui.service.CanvasInitializerService;
import za.co.apricotdb.ui.undo.ApricotUndoManager;

import javax.transaction.Transactional;

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
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ApricotUndoManager undoManager;

    @Autowired
    CanvasInitializerService canvasInitializerService;

    @Autowired
    MapHandler mapHandler;

    @Transactional
    @ApricotInfoLogger
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
    @ApricotInfoLogger
    public void initializeForProject(ApricotProject project) {
        parentWindow.setEmptyEnv(false);
        ApricotSnapshot defaultSnapshot = snapshotManager.getDefaultSnapshot(project);
        initialize(project, defaultSnapshot);
    }

    @Transactional
    @ApricotInfoLogger
    public void initialize(ApricotProject project, ApricotSnapshot snapshot) {
        //  if there is another service in action, just leave the method
        if (canvasInitializerService.getState() == Worker.State.RUNNING) {
            return;
        }

        canvasInitializerService.init("Initialize Project/Snapshot", "Busy re-drawing the canvases...");
        canvasInitializerService.setServiceData(project, snapshot);
        canvasInitializerService.setOnSucceeded(ev -> {
            //  make the current tab selected
            TabPane tabPane = parentWindow.getProjectTabPane();
            Tab currentTab = canvasHandler.getTabOnCurrentView();
            if (currentTab != null) {
                tabPane.getSelectionModel().select(currentTab);
            }

            // initialize the undo stack
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(e -> {
                undoManager.resetUndoBuffer();
                mapHandler.drawMap();
            });
            delay.play();
        });

        canvasInitializerService.start();
    }

    /**
     * Do it, when no projects were registered in the system.
     */
    private void initializeEmptyEnvironment() {
        TreeItem<ProjectExplorerItem> root = new TreeItem<>(
                new ProjectExplorerItem("<No current Project>", ItemType.PROJECT, false));
        parentWindow.getProjectTreeView().setRoot(root);
        ComboBox<String> combo = parentWindow.getSnapshotCombo();
        combo.getItems().clear();
        combo.setValue("<No current Snapshot>");
        TabPane tabPane = parentWindow.getProjectTabPane();
        tabPane.getTabs().clear();
    }
}
