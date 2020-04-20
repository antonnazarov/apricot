package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.RepositoryConfigHandler;
import za.co.apricotdb.ui.repository.RepositoryCell;
import za.co.apricotdb.ui.repository.RepositoryColumnConstructor;
import za.co.apricotdb.ui.repository.RepositoryControl;
import za.co.apricotdb.ui.repository.RepositoryModel;
import za.co.apricotdb.ui.repository.ModelRow;
import za.co.apricotdb.ui.repository.RepositoryRow;
import za.co.apricotdb.ui.repository.RepositoryRowFactory;
import za.co.apricotdb.ui.repository.RowType;

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

    @Autowired
    RepositoryRowFactory rowFactory;

    @Autowired
    RepositoryColumnConstructor columnConstructor;

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

    /**
     * Initialize the controller, using the model data.
     */
    public void init(RepositoryModel model) {
        localApricot.setCellValueFactory(new TreeItemPropertyValueFactory<>("localObject"));
        remoteRepository.setCellValueFactory(new TreeItemPropertyValueFactory<>("remoteObject"));
        compareButtons.setCellValueFactory(new TreeItemPropertyValueFactory<>("control"));

        columnConstructor.construct(localApricot);
        columnConstructor.construct(remoteRepository);

        repositoryView.setShowRoot(false);
        TreeItem<RepositoryRow> root = new TreeItem<RepositoryRow>(
                rowFactory.buildRow(RowType.PROJECT, true, "root node", "root node"));
        repositoryView.setRoot(root);

        TreeItem<RepositoryRow> currentProject = null;
        for (ModelRow mr : model.getRows()) {
            if (mr.getType() == RowType.PROJECT) {
                // the current project has been changed
                if (currentProject != null) {
                    root.getChildren().add(currentProject);
                }
                currentProject = new TreeItem<>(
                        rowFactory.buildRow(mr.getType(), mr.isEqual(), mr.getLocalName(), mr.getRemoteName()));
            } else {
                if (currentProject != null) {
                    TreeItem<RepositoryRow> snap = new TreeItem<>(
                            rowFactory.buildRow(mr.getType(), mr.isEqual(), mr.getLocalName(), mr.getRemoteName()));
                    currentProject.getChildren().add(snap);
                }
            }
        }
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
