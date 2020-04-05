package za.co.apricotdb.ui;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This controller serves the form apricot-repository.fxml.
 * 
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryController {

    @FXML
    Pane mainPane;

    @FXML
    public void configureRepository() {

    }

    @FXML
    public void refresh() {

    }

    @FXML
    public void exit() {
        getStage().close();
    }

    public void init() {

    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
