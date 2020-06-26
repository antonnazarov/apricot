package za.co.apricotdb.ui.handler;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.HtmlViewController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * This service class is responsible for presenting the HTML form in Apricot application.
 *
 * @author Anton Nazarov
 * @since 24/06/2020
 */
@Component
public class HtmlViewHandler {

    @Resource
    ApplicationContext context;

    @ApricotErrorLogger(title = "Unable to show information form")
    public void showHtmlViewForm(String html, String formTitle) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-html-viewer.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = null;
        try {
            window = loader.load();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to open the Html Viewer form", ex);
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(formTitle);
        dialog.getIcons().add(
                new Image(getClass().getResourceAsStream("/za/co/apricotdb/ui/toolbar/tbEditProjectEnabled.png")));
        Scene scene = new Scene(window);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        dialog.setScene(scene);

        HtmlViewController controller = loader.<HtmlViewController>getController();
        controller.init(html);

        dialog.show();
    }

    public void showHtmlViewForm(Map<String, String> values, String templateFile, String formTitle) {
        File file = new File(getClass().getResource("/za/co/apricotdb/ui/handler/" + templateFile).getFile());
        try {
            String template = FileUtils.readFileToString(file, java.nio.charset.Charset.defaultCharset());
            StringSubstitutor substitutor = new StringSubstitutor(values);
            String html = substitutor.replace(template);
            showHtmlViewForm(html, formTitle);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read template: " + templateFile, ex);
        }
    }
}
