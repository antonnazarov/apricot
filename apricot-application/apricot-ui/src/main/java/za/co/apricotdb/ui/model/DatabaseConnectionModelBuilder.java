package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;

@Component
public class DatabaseConnectionModelBuilder {
    
    public DatabaseConnectionModel buildModel(ApricotProject project) {
        DatabaseConnectionModel model = new DatabaseConnectionModel();
        
        return model;
    }
}
