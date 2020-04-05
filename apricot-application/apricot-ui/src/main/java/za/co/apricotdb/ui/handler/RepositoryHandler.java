package za.co.apricotdb.ui.handler;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.ui.RepositoryController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;

/**
 * The Repository related functionality is supported by this component.
 * 
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryHandler {

    @Resource
    ApplicationContext context;

    @ApricotErrorLogger(title = "Unable to create the Apricot Repository forms")
    public void showRepositoryForm() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-repository.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = null;
        try {
            window = loader.load();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Apricot Repository Import/Export");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("???project-2-s1.JPG???")));

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

        RepositoryController controller = loader.<RepositoryController>getController();
        controller.init();

        dialog.show();
    }
}
