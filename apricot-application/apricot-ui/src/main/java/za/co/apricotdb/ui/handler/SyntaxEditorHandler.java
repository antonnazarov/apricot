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
import za.co.apricotdb.ui.SyntaxEditorController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;

/**
 * The Syntax Editor related business logic.
 * 
 * @author Anton Nazarov
 * @since 04/04/2019
 */
@Component
public class SyntaxEditorHandler {

    @Resource
    ApplicationContext context;

    @ApricotErrorLogger(title = "Unable to create the script editing form")
    public void createSyntaxEditorForm(String script, String formHeader) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-syntax-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(formHeader);
        Scene syntaxEditorScene = new Scene(window);
        dialog.setScene(syntaxEditorScene);
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("script-s1.JPG")));
        syntaxEditorScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        SyntaxEditorController controller = loader.<SyntaxEditorController>getController();
        controller.init(script, formHeader);
        dialog.show();
    }
}
