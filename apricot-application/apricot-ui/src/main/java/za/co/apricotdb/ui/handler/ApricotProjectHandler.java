package za.co.apricotdb.ui.handler;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.ui.EditProjectController;
import za.co.apricotdb.ui.OpenProjectController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.model.EditProjectModelBuilder;
import za.co.apricotdb.ui.model.NewProjectModelBuilder;
import za.co.apricotdb.ui.model.ProjectFormModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

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
    
    public void createOpenProjectForm(Pane mainPane)
            throws Exception {
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
}
