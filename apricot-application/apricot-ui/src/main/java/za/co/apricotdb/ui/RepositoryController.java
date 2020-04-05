package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.RepositoryConfigHandler;

/**
 * This controller serves the form apricot-repository.fxml.
 * 
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryController {

    @Autowired
    RepositoryConfigHandler configHandler;

    @FXML
    Pane mainPane;

    @FXML
    public void configureRepository() {
        configHandler.showRepositoryConfigForm();
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
