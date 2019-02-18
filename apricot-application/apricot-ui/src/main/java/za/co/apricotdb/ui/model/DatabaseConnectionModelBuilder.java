package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.persistence.entity.ApricotProject;

@Component
public class DatabaseConnectionModelBuilder {

    public DatabaseConnectionModel buildModel(ApricotProject project) {
        DatabaseConnectionModel model = new DatabaseConnectionModel(
                ApricotTargetDatabase.valueOf(project.getTargetDatabase()));

        return model;
    }
    
    /**
     * Populate the model, based on the current server selected.
     */
    public void populateModel(DatabaseConnectionModel model, String server) {
        
    }
}
