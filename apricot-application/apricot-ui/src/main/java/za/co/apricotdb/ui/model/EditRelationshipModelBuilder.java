package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotTable;

@Component
public class EditRelationshipModelBuilder {
    
    public EditRelationshipModel buildModel(ApricotTable[] selectedTables) {
        EditRelationshipModel model = new EditRelationshipModel(selectedTables);
        
        return model;
    }
}
