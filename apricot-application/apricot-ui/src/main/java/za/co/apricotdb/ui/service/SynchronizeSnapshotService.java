package za.co.apricotdb.ui.service;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.EntityFilterHandler;
import za.co.apricotdb.ui.handler.TabInfoObject;
import za.co.apricotdb.ui.handler.TreeViewHandler;
import za.co.apricotdb.ui.map.MapHandler;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

import java.util.ArrayList;
import java.util.List;

/**
 * This service synchronizes the current snapshot.
 *
 * @author Anton Nazarov
 * @since 20/11/2020
 */
@Component
public class SynchronizeSnapshotService  extends Service<Boolean> implements InitializableService {

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    private boolean synchAllViews;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ViewManager viewManager;

    @Autowired
    EntityFilterHandler filterHandler;

    @Autowired
    MapHandler mapHandler;

    @Override
    protected Task<Boolean> createTask() {

        return new Task<>() {

            @Override
            protected Boolean call() {

                int count = 0;
                int total = 1;
                if (synchAllViews) {
                    total = parentWindow.getProjectTabPane().getTabs().size();
                }
                total += 4;
                updateProgress(++count, total);
                updateMessage("Updating the current snapshot...");

                updateCurrentSnapshot();
                TabInfoObject currentTab = canvasHandler.getCurrentViewTabInfo();
                if (synchAllViews) {
                    TabPane tp = parentWindow.getProjectTabPane();
                    for (Tab tab : tp.getTabs()) {
                        TabInfoObject tabInfo = TabInfoObject.getTabInfo(tab);
                        if (tabInfo != null) {
                            Platform.runLater(() -> {
                                synchronizeViewTab(tabInfo);
                            });
                        }
                        updateProgress(++count, total);
                        updateMessage("Synchronized the view " + tab.getText() + "...");
                    }
                } else {
                    Platform.runLater(() -> {
                        synchronizeViewTab(currentTab);
                    });
                }
                updateProgress(++count, total);
                updateMessage("The views have been synchronized...");

                treeViewHandler.populate(projectManager.findCurrentProject(), snapshotManager.getDefaultSnapshot());
                updateProgress(++count, total);
                updateMessage("The Project Explorer has been refreshed...");

                treeViewHandler.markEntitiesIncludedIntoView(currentTab.getView());
                treeViewHandler.sortEntitiesByView();

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);

        setOnFailed(e -> {
            throw new IllegalArgumentException(getException());
        });

        setOnSucceeded(e -> {
            mapHandler.drawMap();
        });
    }

    public void setServiceData(boolean synchAllViews) {
        this.synchAllViews = synchAllViews;
    }

    /**
     * Re-read the current snapshot from the database and update the TabInfo for all view tabs.
     * It is important if the current snapshot was re-created and replaced.
     */
    private void updateCurrentSnapshot() {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        TabPane tp = parentWindow.getProjectTabPane();
        for (Tab tab : tp.getTabs()) {
            TabInfoObject tabInfo = TabInfoObject.getTabInfo(tab);
            if (tabInfo != null) {
                tabInfo.setSnapshot(snapshot);
            }
        }
    }

    private void synchronizeViewTab(TabInfoObject tabInfo) {
        // re-read the view info
        ApricotView view = viewManager.findViewById(tabInfo.getView().getId());
        tabInfo.setView(view);
        ApricotSnapshot snapshot = tabInfo.getSnapshot();

        List<ApricotTable> tables = canvasHandler.populateCanvas(snapshot, tabInfo.getView(), tabInfo.getCanvas());

        if (filterHandler.isFilterOn()) {
            List<ApricotEntity> filteredEntities = new ArrayList<>(); // the tables which were filtered out
            ApricotCanvas canvas = tabInfo.getCanvas();
            List<ApricotTable> filterTables = filterHandler.getFilterTables();
            for (ApricotTable t : tables) {
                if (!filterTables.contains(t)) {
                    ApricotEntity entity = canvas.findEntityByName(t.getName());
                    if (entity != null) {
                        filteredEntities.add(entity);
                        entity.setElementStatus(ElementStatus.GRAYED);
                    }
                }
            }

            setRelationshipsStatusToGray(filteredEntities);
        }
    }

    /**
     * Set status to GRAYED for the relationships between the entities filtered out.
     */
    private void setRelationshipsStatusToGray(List<ApricotEntity> filteredEntities) {
        for (ApricotEntity entity : filteredEntities) {
            for (ApricotRelationship r : entity.getPrimaryLinks()) {
                if (filteredEntities.contains(r.getChild())) {
                    r.setElementStatus(ElementStatus.GRAYED);
                }
            }
        }
    }
}
