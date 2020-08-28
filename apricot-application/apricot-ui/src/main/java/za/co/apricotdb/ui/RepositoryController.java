package za.co.apricotdb.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.handler.ProgressBarHandler;
import za.co.apricotdb.ui.handler.RepositoryConfigHandler;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.repository.LocalRepoService;
import za.co.apricotdb.ui.repository.ModelRow;
import za.co.apricotdb.ui.repository.RepoCompareService;
import za.co.apricotdb.ui.repository.RepositoryCell;
import za.co.apricotdb.ui.repository.RepositoryColumnConstructor;
import za.co.apricotdb.ui.repository.RepositoryControl;
import za.co.apricotdb.ui.repository.RepositoryModel;
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

    @Autowired
    RepositoryHandler repositoryHandler;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    RepoCompareService compareService;

    @Autowired
    ProgressBarHandler progressBarHandler;

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
        configHandler.showRepositoryConfigForm(false);
    }

    @FXML
    public void refresh() {
        repositoryHandler.refreshModel(true);
    }

    @FXML
    public void exit() {
        getStage().close();
    }

    /**
     * Initialize the controller, using the model data.
     */
    public void init(RepositoryModel model) {
        repositoryView.getStylesheets()
                .add(getClass().getResource("/za/co/apricotdb/ui/apricot-tree-view-solid-grid.css").toExternalForm());

        localApricot.setCellValueFactory(new TreeItemPropertyValueFactory<>("localObject"));
        remoteRepository.setCellValueFactory(new TreeItemPropertyValueFactory<>("remoteObject"));
        compareButtons.setCellValueFactory(new TreeItemPropertyValueFactory<>("control"));

        columnConstructor.construct(localApricot);
        columnConstructor.construct(remoteRepository);

        repositoryView.setShowRoot(false);
        TreeItem<RepositoryRow> root = new TreeItem<>(
                rowFactory.buildRow(RowType.PROJECT, true, "root node", "root node", null));
        repositoryView.setRoot(root);

        for (ModelRow mr : model.getRows()) {
            if (mr.getType() == RowType.PROJECT) {
                TreeItem<RepositoryRow> project = new TreeItem<>(
                        rowFactory.buildRow(mr.getType(), mr.isEqual(), mr.getLocalName(), mr.getRemoteName(), mr));
                root.getChildren().add(project);
                for (ModelRow r : mr.getIncludedItems()) {
                    TreeItem<RepositoryRow> itm = new TreeItem<>(
                            rowFactory.buildRow(r.getType(), r.isEqual(), r.getLocalName(), r.getRemoteName(), r));
                    project.getChildren().add(itm);
                }
            }
        }
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
