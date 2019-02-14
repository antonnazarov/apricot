package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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

        ApricotSnapshot snapshot = new ApricotSnapshot(model.getSnapshotName(), new java.util.Date(), null,
                model.getSnapshotDescription(), true, project, tables);
        snapshot = snapshotManager.saveSnapshot(snapshot);
        
        if(model.getInitSourceSnapshot() != null) {
            tables = cloneSnapshotTables(model.getInitSourceSnapshot(), snapshot);
            if (tables != null) {
                for (ApricotTable t : tables) {
                    // ApricotTable table = tableManager.saveTable(t);
                    System.out.println(t);
                }
            }
        }
        
        // snapshotManager.saveSnapshot(snapshot);
        // snapshotHandler.setDefaultSnapshot(snapshot);

        return snapshot;
    }
    
    /**
     * Make a deep copy of the snapshot tables.
     */
    private List<ApricotTable> cloneSnapshotTables(String snapshotName, ApricotSnapshot snapshot) {
        List<ApricotTable> ret = new ArrayList<>();
        ApricotSnapshot source = snapshotManager.getSnapshotByName(snapshotName);
        
        for (ApricotTable t : source.getTables()) {
            ApricotTable clonedTable = SerializationUtils.clone(t);
            clonedTable.setId(0);
            clonedTable.setSnapshot(snapshot);
            
            ret.add(clonedTable);
        }
        
        return ret;
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

        if (model.isNewSnapshot() && snapshotManager.getSnapshotByName(model.getSnapshotName()) != null) {
            return false;
        }

        return true;
    }
}
