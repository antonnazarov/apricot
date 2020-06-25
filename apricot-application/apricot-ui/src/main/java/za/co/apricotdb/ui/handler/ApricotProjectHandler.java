package za.co.apricotdb.ui.handler;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.EditProjectController;
import za.co.apricotdb.ui.OpenProjectController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.EditProjectModelBuilder;
import za.co.apricotdb.ui.model.NewProjectModelBuilder;
import za.co.apricotdb.ui.model.ProjectFormModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * All the project- related high level business logic is implemented in this
 * class.
 */
@Component
public class ApricotProjectHandler {

    @Resource
    ApplicationContext context;

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
    ApricotSnapshotHandler snapshotHandler;

    @ApricotErrorLogger(title = "Unable to open the list of projects")
    public void createOpenProjectForm(Pane mainPane) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-project-open.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Open Project");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("project-2-s1.JPG")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);
        openProjectScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        OpenProjectController controller = loader.<OpenProjectController>getController();
        controller.init(mainPane);

        dialog.show();
    }

    @ApricotErrorLogger(title = "Unable to create the Edit Project forms")
    public void createEditProjectForm(boolean isCreateNew, Pane mainAppPane) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-project-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        ProjectFormModel model = null;
        if (isCreateNew) {
            dialog.setTitle("Create Project");
            model = newProjectModelBuilder.buildModel();
        } else {
            dialog.setTitle("Edit Project");
            parentWindow.setParentPane(mainAppPane);
            model = editProjectModelBuilder.buildModel(parentWindow.getApplicationData().getCurrentProject());
        }

        dialog.getIcons().add(new Image(getClass().getResourceAsStream("project-2-s1.JPG")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);
        openProjectScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        EditProjectController controller = loader.<EditProjectController>getController();
        controller.init(isCreateNew, model, mainAppPane);

        dialog.show();
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

    /**
     * Compose the map of the give project values.
     */
    public Map<String, String> getProjectValuesMap(ApricotProject project) {
        Map<String, String> ret = new HashMap<>();

        ret.put("project_name", fixEmpty(project.getName()));
        ret.put("database_type", fixEmpty(project.getTargetDatabase()));
        ret.put("erd_notation", fixEmpty(project.getErdNotation().getDefinition()));
        ret.put("description", fixEmpty(project.getDescription()));
        ret.put("snapshot_list", fixEmpty(getSnapshotList(project)));
        ret.put("table_list", fixEmpty(getTableList(project)));
        ret.put("black_list", fixEmpty(getBlackList(project)));

        return ret;
    }

    private String fixEmpty(String s) {
        if (StringUtils.isEmpty(s)) {
            return "NONE";
        }

        return s;
    }

    public String getSnapshotList(ApricotProject project) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ApricotSnapshot s: project.getSnapshots()) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(s.getName());
        }

        return sb.toString();
    }

    public String getTableList(ApricotProject project) {
        ApricotSnapshot snapshot = project.getSnapshots().get(project.getSnapshots().size()-1);

        return snapshotHandler.getTableList(snapshot, 300);
    }

    private String getBlackList(ApricotProject project) {
        for (ApricotProjectParameter pp : project.getParameters()) {
            if (pp.getName().equals(ProjectParameterManager.PROJECT_BLACKLIST_PARAM)) {
                return pp.getValue();
            }
        }

        return null;
    }
}
