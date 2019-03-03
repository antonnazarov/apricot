package za.co.apricotdb.ui.handler;

import java.io.IOException;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.ui.EditEntityController;
import za.co.apricotdb.ui.model.EditEntityModel;
import za.co.apricotdb.ui.model.EditEntityModelBuilder;

/**
 * The handled of Apricot Entity (Table).
 * 
 * @author Anton Nazarov
 * @since 26/02/2019
 */
@Component
public class ApricotEntityHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    EditEntityModelBuilder modelBuilder;

    @Autowired
    ApricotConstraintHandler constraintHandler;

    @Transactional
    public void openEntityEditorForm(boolean newEntity, String tableName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-entity-editor.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        if (newEntity) {
            dialog.setTitle("Create a new Entity");
        } else {
            dialog.setTitle("Edit Entity");
        }
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("table-1-s1.jpg")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);

        EditEntityController controller = loader.<EditEntityController>getController();

        EditEntityModel model = modelBuilder.buildModel(newEntity, tableName);
        controller.init(model);

        dialog.show();
    }
}
