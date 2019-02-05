package za.co.apricotdb.ui;

import za.co.apricotdb.persistence.entity.ApricotProject;

/**
 * This Java- bean contains the overall application data.
 * 
 * @author Anton Nazarov
 * @since 05/02/2019
 */
public class ApplicationData {
    
    private ApricotProject currentProject;

    public ApricotProject getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(ApricotProject currentProject) {
        this.currentProject = currentProject;
    }
}
