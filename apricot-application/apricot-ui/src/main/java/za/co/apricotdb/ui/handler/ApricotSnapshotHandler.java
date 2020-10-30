package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.EditSnapshotController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.model.ApricotSnapshotSerializer;
import za.co.apricotdb.ui.model.EditSnapshotModelBuilder;
import za.co.apricotdb.ui.model.NewSnapshotModelBuilder;
import za.co.apricotdb.ui.model.SnapshotFormModel;
import za.co.apricotdb.ui.service.DeleteSnapshotService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ApricotSnapshotHandler {

    @Autowired
    NewSnapshotModelBuilder newSnapshotModelBuilder;

    @Autowired
    EditSnapshotModelBuilder editSnapshotModelBuilder;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    TreeViewHandler treeViewHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ViewManager viewManager;

    @Autowired
    EntityFilterHandler filterHandler;

    @Autowired
    ApricotSnapshotSerializer snapshotSerializer;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    DialogFormHandler formHandler;

    @Autowired
    DeleteSnapshotService deleteSnapshotService;

    @ApricotErrorLogger(title = "Unable to create the default (empty) snapshot")
    public void createDefaultSnapshot(ApricotProject project) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ApricotSnapshot snapshot = new ApricotSnapshot("Initial snapshot, created " + df.format(new java.util.Date()),
                new java.util.Date(), null, "The snapshot, created by default", true, project,
                new ArrayList<ApricotTable>());
        project.getSnapshots().add(snapshot);
    }

    /**
     * Create the snapshot editing form for a new or existing snapshot.
     */
    @ApricotErrorLogger(title = "Create new Snapshot: unable to create the form")
    public void createEditSnapshotForm(boolean isCreateNew, Pane mainAppPane) {
        String title = null;
        SnapshotFormModel model = null;
        if (isCreateNew) {
            title = "Create Snapshot";
            model = newSnapshotModelBuilder.buildModel();
        } else {
            title = "Edit Snapshot";
            model = editSnapshotModelBuilder.buildModel();
        }
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-snapshot-editor.fxml",
                "snapshot-s1.JPG", title);
        EditSnapshotController controller = form.getController();
        controller.init(model);

        form.show();
    }

    @ApricotErrorLogger(title = "Unable to delete the current snapshot")
    public void deleteSnapshot() {
        ApricotProject project = projectManager.findCurrentProject();
        List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
        if (snapshots.size() == 1) {
            Alert alert = getAlert(AlertType.WARNING, WordUtils.wrap("The snapshot \"" + snapshots.get(0).getName()
                    + "\" is the only snapshot in the project \"" + project.getName()
                    + "\". This snapshot can't be deleted, since a project has to include at least one snapshot.", 60));
            alertDecorator.decorateAlert(alert);
            alert.showAndWait();

            return;
        }

        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();

        ButtonType yes = new ButtonType("Delete", ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING, null, yes, no);
        alert.setTitle("Delete Snapshot");
        alert.setHeaderText("Do you really want to delete the snapshot \"" + snapshot.getName() + "\"?");
        alert.initOwner(parentWindow.getWindow());
        alertDecorator.decorateAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            prepareDeleteSnapshotService(snapshot);
            deleteSnapshotService.start();
        }
    }

    private void prepareDeleteSnapshotService(ApricotSnapshot snapshot) {
        //  transfer the input parameter
        deleteSnapshotService.reset();
        deleteSnapshotService.setSnapshotId(snapshot.getId());
        //  define post Snapshot Delete operations
        deleteSnapshotService.setOnSucceeded(e -> {
            List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(snapshot.getProject());
            snapshotManager.setDefaultSnapshot(snapshots.get(snapshots.size() - 1));
            applicationInitializer.initializeDefault();
        });

        deleteSnapshotService.setOnFailed(e -> {
            throw new IllegalArgumentException(deleteSnapshotService.getException());
        });

        deleteSnapshotService.init("Delete Snapshot",
                "The deletion of the snapshot \"" + snapshot.getName() + "\" is in progress");
    }

    /**
     * Re-draw all the views for the current (default) snapshot together with the
     * tree view representation of the data
     */
    @ApricotErrorLogger(title = "Unable to synchronize the Snapshot")
    public void syncronizeSnapshot(boolean synchAllViews) {
        syncSnapshotTransactional(synchAllViews);
    }

    @ApricotErrorLogger(title = "Unable to save the current Snapshot")
    public boolean serializeSnapshot(SnapshotFormModel model) {
        if (snapshotSerializer.serializeSnapshot(model)) {
            applicationInitializer.initializeForProject(projectManager.findCurrentProject());

            return true;
        }

        return false;
    }

    @Transactional
    public void syncSnapshotTransactional(boolean synchAllViews) {
        TabInfoObject currentTab = canvasHandler.getCurrentViewTabInfo();
        if (synchAllViews) {
            TabPane tp = parentWindow.getProjectTabPane();
            for (Tab tab : tp.getTabs()) {
                TabInfoObject tabInfo = TabInfoObject.getTabInfo(tab);
                if (tabInfo != null) {
                    synchronizeViewTab(tabInfo);
                }
            }
        } else {
            synchronizeViewTab(currentTab);
        }

        treeViewHandler.populate(projectManager.findCurrentProject(), snapshotManager.getDefaultSnapshot());
        treeViewHandler.markEntitiesIncludedIntoView(currentTab.getView());
        treeViewHandler.sortEntitiesByView();
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

    private Alert getAlert(AlertType alertType, String text) {
        Alert alert = new Alert(alertType, null, ButtonType.OK);
        alert.setTitle("Delete Snapshot");
        alert.setHeaderText(text);
        alert.initOwner(parentWindow.getWindow());
        if (alertType == AlertType.ERROR) {
            alertDecorator.decorateAlert(alert);
        }

        return alert;
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
