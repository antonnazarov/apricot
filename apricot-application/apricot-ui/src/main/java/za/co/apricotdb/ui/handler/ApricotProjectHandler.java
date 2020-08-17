package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.ui.EditProjectController;
import za.co.apricotdb.ui.OpenProjectController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.model.EditProjectModelBuilder;
import za.co.apricotdb.ui.model.NewProjectModelBuilder;
import za.co.apricotdb.ui.model.ProjectFormModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * All the project- related high level business logic is implemented in this
 * class.
 */
@Component
public class ApricotProjectHandler {

    @Autowired
    NewProjectModelBuilder newProjectModelBuilder;

    @Autowired
    EditProjectModelBuilder editProjectModelBuilder;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    DialogFormHandler formHandler;

    @ApricotErrorLogger(title = "Unable to open the list of projects")
    public void createOpenProjectForm(Pane mainPane) {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-project-open.fxml",
                "project-2-s1.JPG", "Open Project");
        OpenProjectController controller = form.getController();
        controller.init(mainPane);

        form.show();
    }

    @ApricotErrorLogger(title = "Unable to create the Edit Project forms")
    public void createEditProjectForm(boolean isCreateNew, Pane mainAppPane) throws Exception {

        ProjectFormModel model = null;
        String title = null;
        if (isCreateNew) {
            title = "Create Project";
            model = newProjectModelBuilder.buildModel();
        } else {
            title = "Edit Project";
            parentWindow.setParentPane(mainAppPane);
            model = editProjectModelBuilder.buildModel(parentWindow.getApplicationData().getCurrentProject());
        }

        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-project-editor.fxml",
                "project-2-s1.JPG", title);
        EditProjectController controller = form.getController();
        controller.init(isCreateNew, model, mainAppPane);

        form.show();
    }

    @Transactional
    public boolean deleteCurrentProject() {
        ApricotProject project = projectManager.findCurrentProject();

        ButtonType yes = new ButtonType("Delete", ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING, null, yes, no);
        alert.setTitle("Delete Project");
        alert.setHeaderText("Do you want to delete the project \"" + project.getName() + "\"?");
        alertDecorator.decorateAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            projectManager.deleteProject(project);
            List<ApricotProject> prj = projectManager.getAllProjects();
            if (prj != null && prj.size() > 0) {
                projectManager.setProjectCurrent(prj.get(0));
            }

            return true;
        }

        return false;
    }

    @ApricotErrorLogger(title = "Unable to delete the current Project")
    public void deleteProject() {
        if (deleteCurrentProject()) {
            applicationInitializer.initializeDefault();
        }
    }

    @ApricotErrorLogger(title = "Unable to open the selected Project")
    public void openProject(TableView<ApricotProject> projectsList) {
        ApricotProject selectedProject = projectsList.getSelectionModel().getSelectedItem();
        projectManager.setProjectCurrent(selectedProject);
        applicationInitializer.initializeDefault();
    }
}
