package za.co.apricotdb.ui.model;

import java.io.Serializable;

/**
 * The model- object for the New/Edit Project- form.
 * 
 * @author Anton Nazarov
 * @since 05/02/2019
 */
public class ProjectFormModel implements Serializable {

    private static final long serialVersionUID = -5353358137642361288L;
    
    private boolean newView;
    private String projectName;
    private String projectDescription;
    private String projectDatabase;
    private String projectBlackList;
    private long projectId;
    private String blackList;
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getProjectDescription() {
        return projectDescription;
    }
    
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
    
    public String getProjectDatabase() {
        return projectDatabase;
    }
    
    public void setProjectDatabase(String projectDatabase) {
        this.projectDatabase = projectDatabase;
    }
    
    public String getProjectBlackList() {
        return projectBlackList;
    }
    
    public void setProjectBlackList(String projectBlackList) {
        this.projectBlackList = projectBlackList;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public boolean isNewView() {
        return newView;
    }

    public void setNewView(boolean newView) {
        this.newView = newView;
    }

    public String getBlackList() {
        return blackList;
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
    }
}
