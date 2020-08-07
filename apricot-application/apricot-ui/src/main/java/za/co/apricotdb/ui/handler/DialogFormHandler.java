package za.co.apricotdb.ui.handler;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.util.ImageHelper;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * The helper component to create a dialog pane with FXML loader.
 *
 * @author Anton Nazarov
 * @since 06/08/2020
 */
@Component
public class DialogFormHandler {

    @Resource
    ApplicationContext context;

    public ApricotForm buildApricotForm(String formFile, String imageFile, String formTitle) {
        FXMLLoader loader = getLoader(formFile);
        Stage dialog = buildForm(loader, imageFile, formTitle);

        return new ApricotForm(loader, dialog);
    }

    private FXMLLoader getLoader(String formFile) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(formFile));
        loader.setControllerFactory(context::getBean);

        return loader;
    }

    private Stage buildForm(FXMLLoader loader, String imageFile, String formTitle) {
        Scene scene = null;
        try {
            Pane window = loader.load();
            scene = new Scene(window);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load the form", ex);
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(formTitle);
        if (imageFile != null) {
            dialog.getIcons().add(ImageHelper.getImage(imageFile, getClass()));
        }
        dialog.setScene(scene);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        return dialog;
    }
}
