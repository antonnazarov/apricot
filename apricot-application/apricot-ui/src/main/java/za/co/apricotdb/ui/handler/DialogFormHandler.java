package za.co.apricotdb.ui.handler;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ParentWindow;
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

    @Autowired
    ParentWindow pw;

    public ApricotForm buildApricotForm(String formFile, String imageFile, String formTitle,
                                        EventHandler<KeyEvent> eventHandler) {
        FXMLLoader loader = getLoader(formFile);
        Stage dialog = buildForm(loader, imageFile, formTitle, eventHandler);

        return new ApricotForm(loader, dialog);
    }

    public ApricotForm buildApricotForm(String formFile, String imageFile, String formTitle) {
        return buildApricotForm(formFile, imageFile, formTitle, null);
    }

    private FXMLLoader getLoader(String formFile) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(formFile));
        loader.setControllerFactory(context::getBean);

        return loader;
    }

    private Stage buildForm(FXMLLoader loader, String imageFile, String formTitle,
                            EventHandler<KeyEvent> eventHandler) {
        Scene scene = null;
        try {
            Pane window = loader.load();
            scene = new Scene(window);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load the form", ex);
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pw.getPrimaryStage());
        dialog.setTitle(formTitle);
        if (imageFile != null) {
            dialog.getIcons().add(ImageHelper.getImage(imageFile, getClass()));
        }
        dialog.setScene(scene);

        if (eventHandler == null) {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        dialog.close();
                    }
                }
            });
        } else {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
        }

        return dialog;
    }
}
