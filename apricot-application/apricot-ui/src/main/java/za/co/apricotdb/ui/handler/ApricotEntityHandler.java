package za.co.apricotdb.ui.handler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.EditEntityController;
import za.co.apricotdb.ui.model.EditEntityModel;
import za.co.apricotdb.ui.model.EditEntityModelBuilder;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

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

    @Autowired
    AlertMessageDecorator alertDecorator;

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

    public boolean requestColumnDelete(ApricotColumn column) {
        List<ApricotConstraint> constr = constraintHandler.getConstraintsForColumn(column);
        
        if (constr.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (ApricotConstraint c : constr) {
                sb.append(c.getType().name()).append(" (").append(c.getName()).append(")\n");
            }
            
            ButtonType yes = new ButtonType("Delete", ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(AlertType.WARNING, null, yes, no);
            alert.setTitle("Delete Column");
            alert.setHeaderText("The following constraints, attached to the column \"" + column.getName() + "\" will be deleted:\n" + sb.toString());
            alertDecorator.decorateAlert(alert);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {
                return true;
            } else {
                return false;
            }
        }
        
        return true;
    }
}
