package za.co.apricotdb.ui.handler;

import java.beans.PropertyChangeListener;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.ui.EditProjectController;
import za.co.apricotdb.ui.OpenProjectController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.model.EditProjectModelBuilder;
import za.co.apricotdb.ui.model.NewProjectModelBuilder;
import za.co.apricotdb.ui.model.ProjectFormModel;

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
    
    public void createOpenProjectForm(BorderPane mainBorderPane, PropertyChangeListener canvasChangeListener)
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

        OpenProjectController controller = loader.<OpenProjectController>getController();
        controller.init(mainBorderPane, canvasChangeListener);

        dialog.show();
    }
    
    public void createEditProjectForm(boolean isCreateNew, BorderPane mainBorderPane,
            PropertyChangeListener canvasChangeListener) throws Exception {
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
            parentWindow.setParentPane(mainBorderPane);
            model = editProjectModelBuilder.buildModel(parentWindow.getApplicationData().getCurrentProject());
        }
        
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("project-2-s1.JPG")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        EditProjectController controller = loader.<EditProjectController>getController();
        controller.init(isCreateNew, model, mainBorderPane, canvasChangeListener);

        dialog.show();
    }
}
