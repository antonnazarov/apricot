package za.co.apricotdb.ui.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * The validator of Snapshot information.
 * 
 * @author Anton Nazarov
 * @since 27/03/2019
 */
@Component
public class ApricotSnapshotValidator {
    
    @Autowired
    AlertMessageDecorator alertDecorator;
    
    @Autowired
    SnapshotManager snapshotManager;
    
    @Autowired
    ProjectManager projectManager;
    
    public boolean validate(SnapshotFormModel model) {
        String name = model.getSnapshotName();
        if (name == null || name.equals("") || name.equals("<New Snapshot>")) {
            Alert alert = alertDecorator.getErrorAlert("Edit Snapshot", "The Snapshot must have a non empty unique name");
            alert.showAndWait();

            return false;
        }
        
        if (model.isNewSnapshot() && snapshotManager.getSnapshotByName(projectManager.findCurrentProject(), name) != null) {
            Alert alert = alertDecorator.getErrorAlert("Edit Snapshot", "The Snapshot \"" + name + "\" is not unique within the current Project");
            alert.showAndWait();

            return false;
        }
        
        return true;
    }
}
