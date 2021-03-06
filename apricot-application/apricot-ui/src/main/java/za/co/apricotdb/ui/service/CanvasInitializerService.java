package za.co.apricotdb.ui.service;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.TreeViewHandler;

import java.util.List;

/**
 * This service initializes the canvases in the dedicated thread other that the main JavaFX thread.
 *
 * @author Anton Nazarov
 * @since 19/11/2020
 */
@Component
public class CanvasInitializerService extends Service<Boolean> implements InitializableService {

    private static final Logger logger = LoggerFactory.getLogger(CanvasInitializerService.class);

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ApricotViewHandler viewHandler;

    private ApricotProject project;
    private ApricotSnapshot snapshot;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {

            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                int count = 0;
                updateProgress(0, 5);
                updateMessage("Reading the project data...");
                project = projectManager.getProjectByName(project.getName());
                snapshot = snapshotManager.getSnapshotByName(project, snapshot.getName());
                List<ApricotView> views = viewHandler.getAllViews(project);
                int total = views.size() + 3;

                // clear the current filter
                parentWindow.getFilterTables().clear();
                parentWindow.getFilterField().setText("*");

                // remember the current project
                parentWindow.getApplicationData().setCurrentProject(project);
                updateProgress(++count, total);
                updateMessage("The current project has been set...");

                Platform.runLater(() -> {
                    treeViewHandler.populate(project, snapshot);
                });
                updateProgress(++count, total);
                updateMessage("The project explorer has been populated...");

                Platform.runLater(() -> {
                    ComboBox<String> combo = parentWindow.getSnapshotCombo();
                    if (combo.getUserData() != null && combo.getUserData().equals("snapshotCombo.selectSnapshot")) {
                        combo.setUserData("reset");
                    } else {
                        combo.setUserData("AppInitialize");
                        initCombo(project, snapshot, combo);
                    }
                });

                TabPane tabPane = parentWindow.getProjectTabPane();
                Platform.runLater(() -> {
                    tabPane.getTabs().clear();
                });
                for (ApricotView view : views) {
                    updateProgress(++count, total);
                    updateMessage("The view " + view.getName() + " being populated...");

                    // create Tabs for General view and for other views with the Layout Objects
                    if (view.isGeneral() || viewManager.getLayouts(view).size() != 0) {
                        Platform.runLater(() -> {
                            viewHandler.createViewTab(snapshot, view, tabPane);
                        });
                    }
                }

                logger.info("CanvasInitializerService: " + (System.currentTimeMillis()-start) + " ms");

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);

        this.setOnFailed(e -> {
            throw new IllegalArgumentException(getException());
        });
    }

    public void setServiceData(ApricotProject project, ApricotSnapshot snapshot) {
        this.project = project;
        this.snapshot = snapshot;
    }

    private void initCombo(ApricotProject project, ApricotSnapshot snapshot, ComboBox<String> combo) {
        combo.getItems().clear();
        List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
        for (ApricotSnapshot s : snapshots) {
            combo.getItems().add(s.getName());
        }
        combo.setValue(snapshot.getName());
    }
}
