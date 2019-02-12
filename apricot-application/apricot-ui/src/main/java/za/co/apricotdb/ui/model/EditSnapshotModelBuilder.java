package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

/**
 * Builder of the model of the existing snapshot.
 * 
 * @author Anton Nazarov
 * @since 11/02/2019
 */
@Component
public class EditSnapshotModelBuilder {

    public SnapshotFormModel buildModel() {
        
        SnapshotFormModel model = new SnapshotFormModel();
        model.setNewSnapshot(false);
        
        return model;
    }
}
