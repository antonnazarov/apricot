package za.co.apricotdb.ui.handler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.util.ImageHelper;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * This is a handler that created the Apricot About form.
 *
 * @author Anton Nazarov
 * @since 01/07/2019
 */
@Component
public class ApricotAboutHandler {

    @Resource
    ApplicationContext context;

    public void showAboutForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-about-form.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("About Apricot DB");
        Scene scene = new Scene(window);
        dialog.setScene(scene);
        dialog.getIcons().add(ImageHelper.getImage("favicon-32x32.png", getClass()));
        dialog.show();
    }
}
