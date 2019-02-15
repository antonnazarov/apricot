package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import za.co.apricotdb.persistence.data.SnapshotCloneManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;

/**
 * All snapshot serialisation operations have been contained in this component.
 * 
 * @author Anton Nazarov
 * @since 13/02/2019
 */
@Component
public class ApricotSnapshotSerializer {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotCloneManager snapshotCloneManager;

    public boolean validate(SnapshotFormModel model) {
        if (!validateName(model)) {
            Alert alert = getAlert("Please enter a unique name of the snapshot");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    @Transactional
    public ApricotSnapshot serializeSnapshot(SnapshotFormModel model) {
        ApricotSnapshot ret = null;
        if (model.isNewSnapshot()) {
            ret = serializeNewSnapshot(model);
        } else {
            ret = serializeEditedSnapshot(model);
        }

        return ret;
    }

    private ApricotSnapshot serializeNewSnapshot(SnapshotFormModel model) {
        ApricotProject project = parentWindow.getApplicationData().getCurrentProject();
        List<ApricotTable> tables = new ArrayList<>();

        ApricotSnapshot snapshot = null;
        if (model.getInitSourceSnapshot() != null) {
            ApricotSnapshot sourceSnapshot = snapshotManager.getSnapshotByName(project, model.getInitSourceSnapshot());
            snapshot = snapshotCloneManager.cloneSnapshot(model.getSnapshotName(), model.getSnapshotDescription(),
                    project, sourceSnapshot);
        } else {
            snapshot = new ApricotSnapshot(model.getSnapshotName(), new java.util.Date(), null,
                    model.getSnapshotDescription(), false, project, tables);
            snapshot = snapshotManager.saveSnapshot(snapshot);
        }

        snapshotHandler.setDefaultSnapshot(snapshot);

        return snapshot;
    }

    private ApricotSnapshot serializeEditedSnapshot(SnapshotFormModel model) {
        ApricotSnapshot snapshot = null;

        return snapshot;
    }

    private Alert getAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, text, ButtonType.OK);
        alert.setTitle("Save Snapshot");
        alert.setHeaderText("Unable to save the snapshot");

        return alert;
    }

    /**
     * Check if the view- name is unique.
     */
    private boolean validateName(SnapshotFormModel model) {
        if (model.getSnapshotName() == null || model.getSnapshotName().equals("")
                || model.getSnapshotName().equals("<New Snapshot>")) {
            return false;
        }

        if (model.isNewSnapshot()
                && snapshotManager.getSnapshotByName(parentWindow.getApplicationData().getCurrentProject(),
                        model.getSnapshotName()) != null) {
            return false;
        }

        return true;
    }
}
