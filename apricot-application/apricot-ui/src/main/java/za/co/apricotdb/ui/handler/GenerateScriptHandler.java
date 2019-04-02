package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
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
import za.co.apricotdb.ui.DBScriptType;
import za.co.apricotdb.ui.ScriptGenerateController;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The Generate Script- related business logic.
 * 
 * @author Anton Nazarov
 * @since 02/04/2019
 */
@Component
public class GenerateScriptHandler {

    @Resource
    ApplicationContext context;
    
    @Autowired
    ApricotCanvasHandler canvasHandler;

    public void createGenerateScriptForm(DBScriptType scriptType) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-generate-script.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        String formHeader = null;
        switch (scriptType) {
        case CREATE_SCRIPT:
            formHeader = "Generate CREATE- Script";
            break;
        case DELETE_SCRIPT:
            formHeader = "Generate DELETE- Script";
            break;
        case DROP_SCRIPT:
            formHeader = "Generate DROP- Script";
            break;
        default:
            break;
        }

        dialog.setTitle(formHeader);

        Scene generateScriptScene = new Scene(window);
        dialog.setScene(generateScriptScene);
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("script-s1.JPG")));
        generateScriptScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        ScriptGenerateController controller = loader.<ScriptGenerateController>getController();
        
        controller.init(scriptType, getSelectedEntities().size()>0);

        dialog.show();
    }
    
    public List<ApricotEntity> getSelectedEntities() {
        ApricotCanvas canvas = canvasHandler.getSelectedCanvas();
        
        return canvas.getSelectedEntities();
    }
}
