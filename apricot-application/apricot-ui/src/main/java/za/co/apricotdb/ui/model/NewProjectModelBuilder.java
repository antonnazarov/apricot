package za.co.apricotdb.ui.model;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.ApricotTargetDatabases;

@Component
public class NewProjectModelBuilder {

    public ProjectFormModel buildModel() {
        
        ProjectFormModel model = new ProjectFormModel();
        model.setProjectName("<New Project>");
        //  MS SQL Server by default
        model.setProjectDatabase(ApricotTargetDatabases.MSSQLServer.toString());
        
        return model;
    }
}
