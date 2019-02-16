package za.co.apricotdb.ui.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * Builder of the model of the existing snapshot.
 * 
 * @author Anton Nazarov
 * @since 11/02/2019
 */
@Component
public class EditSnapshotModelBuilder {
    
    @Autowired
    SnapshotManager snapshotManager;
    
    public SnapshotFormModel buildModel() {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        
        SnapshotFormModel model = new SnapshotFormModel();
        model.setNewSnapshot(false);
        model.setSnapshotName(snapshot.getName());
        model.setSnapshotDescription(snapshot.getComment());
        
        return model;
    }
}
