package za.co.apricotdb.ui.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.EditSnapshotController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.model.EditSnapshotModelBuilder;
import za.co.apricotdb.ui.model.NewSnapshotModelBuilder;
import za.co.apricotdb.ui.model.SnapshotFormModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

@Component
public class ApricotSnapshotHandler {

    @Resource
    ApplicationContext context;

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
    public void createEditSnapshotForm(boolean isCreateNew, Pane mainAppPane) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-snapshot-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        SnapshotFormModel model = null;
        if (isCreateNew) {
            dialog.setTitle("Create Snapshot");
            model = newSnapshotModelBuilder.buildModel();
        } else {
            dialog.setTitle("Edit Snapshot");
            model = editSnapshotModelBuilder.buildModel();
        }

        dialog.getIcons().add(new Image(getClass().getResourceAsStream("snapshot-s1.JPG")));

        Scene editSnapshotScene = new Scene(window);
        dialog.setScene(editSnapshotScene);
        editSnapshotScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        EditSnapshotController controller = loader.<EditSnapshotController>getController();
        controller.init(model);

        dialog.show();
    }

    public boolean deleteSnapshot() {
        ApricotProject project = projectManager.findCurrentProject();
        List<ApricotSnapshot> snaps = snapshotManager.getAllSnapshots(project);
        if (snaps.size() == 1) {
            Alert alert = getAlert(AlertType.WARNING, WordUtils.wrap("The snapshot \"" + snaps.get(0).getName()
                    + "\" is the only snapshot in the project \"" + project.getName()
                    + "\". This snapshot can't be deleted, since a project has to include at least one snapshot.", 60));
            alertDecorator.decorateAlert(alert);
            alert.showAndWait();

            return false;
        }

        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();

        ButtonType yes = new ButtonType("Delete", ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING, null, yes, no);
        alert.setTitle("Delete Snapshot");
        alert.setHeaderText("Do you really want to delete the snapshot \"" + snapshot.getName() + "\"?");
        alertDecorator.decorateAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            snapshotManager.deleteSnapshot(snapshot);
            List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
            ApricotSnapshot defSnapshot = snapshots.get(snapshots.size() - 1);
            snapshotManager.setDefaultSnapshot(defSnapshot);

            return true;
        }

        return false;
    }

    /**
     * Re-draw all the views for the current (default) snapshot together with the
     * tree view representation of the data
     */
    @Transactional
    public void syncronizeSnapshot(boolean synchAllViews) {
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
