package za.co.apricotdb.ui.handler;

import java.beans.PropertyChangeListener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.ui.OpenProjectController;

/**
 * All the project- related high level business logic is implemented in this
 * class.
 */
@Component
public class ApricotProjectHandler {

    @Resource
    ApplicationContext context;

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
    
    public void createEditProjectForm() {
        
    }
}
