package za.co.apricotdb.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.RepositoryConfigHandler;
import za.co.apricotdb.ui.repository.RepositoryCell;
import za.co.apricotdb.ui.repository.RepositoryControl;
import za.co.apricotdb.ui.repository.RepositoryRow;

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
    TreeTableView<RepositoryRow> repositoryView;
    
    @FXML
    TreeTableColumn<RepositoryRow, RepositoryCell> localApricot;

    @FXML
    TreeTableColumn<RepositoryRow, RepositoryControl> compareButtons;
    
    @FXML
    TreeTableColumn<RepositoryRow, RepositoryCell> remoteRepository;
    
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

    public void init(List<RepositoryRow> repoRows) {
        
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
