package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

/**
 * The builder of the new snapshot model.
 * 
 * @author Anton Nazarov
 * @since 11/02/2019
 */
@Component
public class NewSnapshotModelBuilder {
    
    public SnapshotFormModel buildModel() {
        
        SnapshotFormModel model = new SnapshotFormModel();
        
        model.setSnapshotName("<New Snapshot>");
        model.setInitializedFromSnapshot(false);
        model.setNewSnapshot(true);
        
        return model;
    }
}
