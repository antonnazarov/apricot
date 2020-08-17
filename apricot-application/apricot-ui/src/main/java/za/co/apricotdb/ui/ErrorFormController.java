package za.co.apricotdb.ui;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The controller of the form apricot-error-details-form.fxml
 * 
 * @author Anton Nazarov
 * @since 16/01/2020
 */
@Component
public class ErrorFormController {

    @Resource
    ApplicationContext context;

    @Autowired
    ParentWindow pw;

    @FXML
    TextArea errorText;

    @FXML
    Pane mainPane;

    @FXML
    public void close(ActionEvent event) {
        getStage().close();
    }

    public void openForm(String text) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-error-details-form.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pw.getPrimaryStage());
        dialog.setTitle("The error details");

        Scene scene = new Scene(window);
        dialog.setScene(scene);

        ErrorFormController controller = loader.<ErrorFormController>getController();
        controller.init(text);

        dialog.show();
    }

    public void init(String text) {
        errorText.setText(text);
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
