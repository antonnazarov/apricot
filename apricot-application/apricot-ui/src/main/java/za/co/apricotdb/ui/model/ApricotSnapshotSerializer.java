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
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * All snapshot serialization operations have been contained in this component.
 * 
 * @author Anton Nazarov
 * @since 13/02/2019
 */
@Component
public class ApricotSnapshotSerializer {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    TableManager tableManager;

    @Autowired
    SnapshotCloneManager snapshotCloneManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ApricotSnapshotValidator snapshotValidator;

    public boolean validate(SnapshotFormModel model) {
        if (!validateName(model)) {
            Alert alert = getAlert("Please enter a name of the snapshot");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    @Transactional
    public boolean serializeSnapshot(SnapshotFormModel model) {
        if (!snapshotValidator.validate(model)) {
            return false;
        }

        if (model.isNewSnapshot()) {
            serializeNewSnapshot(model);
        } else {
            serializeEditedSnapshot(model);
        }

        return true;
    }

    private ApricotSnapshot serializeNewSnapshot(SnapshotFormModel model) {
        ApricotProject project = parentWindow.getApplicationData().getCurrentProject();
        List<ApricotTable> tables = new ArrayList<>();

        ApricotSnapshot snapshot = null;
        String snapshotDescription = (model.getSnapshotDescription() == null
                || model.getSnapshotDescription().length() == 0 ? "" : model.getSnapshotDescription() + "\n");

        if (model.getInitSourceSnapshot() != null) {
            snapshotDescription += "The Snapshot was created as a copy of \"" + model.getInitSourceSnapshot() + "\".";
            ApricotSnapshot sourceSnapshot = snapshotManager.getSnapshotByName(project, model.getInitSourceSnapshot());
            snapshot = snapshotCloneManager.cloneSnapshot(model.getSnapshotName(), snapshotDescription, project,
                    sourceSnapshot);
        } else {
            snapshotDescription += "The Snapshot was created as empty";
            snapshot = new ApricotSnapshot(model.getSnapshotName(), new java.util.Date(), null, snapshotDescription,
                    false, project, tables);
            snapshot = snapshotManager.saveSnapshot(snapshot);
        }

        snapshotManager.setDefaultSnapshot(snapshot);

        return snapshot;
    }

    private ApricotSnapshot serializeEditedSnapshot(SnapshotFormModel model) {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        snapshot.setName(model.getSnapshotName());
        snapshot.setComment(model.getSnapshotDescription());
        snapshot.setUpdated(new java.util.Date());

        return snapshotManager.saveSnapshot(snapshot);
    }

    private Alert getAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle("Save Snapshot");
        alert.setHeaderText(text);
        alert.initOwner(parentWindow.getWindow());
        alertDecorator.decorateAlert(alert);

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
