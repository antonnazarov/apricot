package za.co.apricotdb.ui.model;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

@Component
public class ApricotProjectSerializer {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    AlertMessageDecorator alertDecorator;
    
    @Autowired
    ApplicationInitializer applicationInitializer;

    @Transactional
    public ApricotProject serializeNewProject(ProjectFormModel model) {
        ApricotProject p = new ApricotProject(model.getProjectName(), model.getProjectDescription(),
                model.getProjectDatabase(), true, new java.util.Date(), new ArrayList<ApricotSnapshot>(),
                new ArrayList<ApricotProjectParameter>(), new ArrayList<ApricotView>(), model.getErdNotation());

        snapshotHandler.createDefaultSnapshot(p);
        viewHandler.createDefaultView(p);
        ApricotProject ret = projectManager.saveApricotProject(p);

        return ret;
    }

    @Transactional
    public ApricotProject serializeEditedProject(ProjectFormModel model) {
        ApricotProject project = projectManager.getProject(model.getProjectId());
        project.setName(model.getProjectName());
        project.setDescription(model.getProjectDescription());
        project.setTargetDatabase(model.getProjectDatabase());
        project.setErdNotation(model.getErdNotation());

        return projectManager.saveApricotProject(project);
    }
    
    @ApricotErrorLogger(title = "Unable to save the Project")
    public void serializeProject(ProjectFormModel model, boolean isCreateNew) {
        if (!validate(model)) {
            return;
        }
        
        ApricotProject project = null;
        if (isCreateNew) {
            project = serializeNewProject(model);
        } else {
            project = serializeEditedProject(model);
        }

        applicationInitializer.initializeForProject(project);
        if (isCreateNew) {
            projectManager.setProjectCurrent(project);
        }
    }

    public boolean validate(ProjectFormModel model) {
        if (!validateName(model)) {
            Alert alert = getAlert("Please enter a unique and non empty name of the project");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    /**
     * Check if the project- name is correct.
     */
    private boolean validateName(ProjectFormModel model) {

        if (model.getProjectName() == null || model.getProjectName().equals("")
                || model.getProjectName().equals("<New Project>")) {
            return false;
        }

        if (model.isNewView()) {
            ApricotProject p = projectManager.getProjectByName(model.getProjectName());
            if (p != null) {
                return false;
            }
        }

        return true;
    }

    private Alert getAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle("Save Project");
        alert.setHeaderText(text);
        alertDecorator.decorateAlert(alert);

        return alert;
    }
}
