package za.co.apricotdb.ui.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * All operations required for Black List have been supported by this component.
 *  
 * @author Anton Nazarov
 * @since 19/02/2019
 */
@Component
public class BlackListHandler {
    
    @Autowired
    ProjectParameterManager projectParameterManager;
    
    public String[] getBlackListTables(ApricotProject project) {
        String[] ret = new String[] {};
        ApricotProjectParameter p = projectParameterManager.getParameterByName(project, ProjectParameterManager.PROJECT_BLACKLIST_PARAM);
        if (p != null) {
            ret = p.getValue().split(";");
        }
        
        return ret;
    }
    
    public void saveBlackList(ApricotProject project, List<ApricotTable> tables) {
        StringBuilder sb = new StringBuilder();
        for (ApricotTable t : tables) {
            if (sb.length() != 0) {
                sb.append(";");
            }
            sb.append(t.getName());
            
            projectParameterManager.saveParameter(project, ProjectParameterManager.PROJECT_BLACKLIST_PARAM, sb.toString());
        }
    }
}
