package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.ApricotTargetDatabase;

@Component
public class NewProjectModelBuilder {

    public ProjectFormModel buildModel() {
        
        ProjectFormModel model = new ProjectFormModel();
        model.setProjectName("<New Project>");
        //  MS SQL Server by default
        model.setProjectDatabase(ApricotTargetDatabase.MSSQLServer.toString());
        
        return model;
    }
}
