package za.co.apricotdb.ui.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.ui.handler.BlackListHandler;

@Component
public class EditProjectModelBuilder {
    
    @Autowired
    ProjectManager projectManager;
    
    @Autowired
    BlackListHandler blackListHandler;
    
    public ProjectFormModel buildModel(ApricotProject currentProject) {
        
        ApricotProject p = projectManager.getProject(currentProject.getId());
        String blackList = blackListHandler.getBlackListAsString(p);
        
        ProjectFormModel model = new ProjectFormModel();
        model.setProjectName(p.getName());
        model.setProjectDescription(p.getDescription());
        model.setProjectDatabase(p.getTargetDatabase());
        model.setErdNotation(p.getErdNotation());
        model.setProjectId(currentProject.getId());
        model.setBlackList(blackList);
        
        return model;
    }
}
