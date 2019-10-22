package za.co.apricotdb.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import za.co.apricotdb.persistence.comparator.ColumnDifference;
import za.co.apricotdb.persistence.comparator.SnapshotDifference;
import za.co.apricotdb.persistence.comparator.TableDifference;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.handler.CompareSnapshotsHandler;

/**
 * This is a controller, which serves the apricot-compare-snapshots.fxml form.
 * 
 * @author Anton Nazarov
 * @since 15/10/2019
 */
@Component
public class CompareSnapshotsController {

    Logger logger = LoggerFactory.getLogger(CompareSnapshotsController.class);

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    CompareSnapshotsHandler compareSnapshotsHandler;

    @FXML
    ChoiceBox<String> sourceSnapshot;

    @FXML
    ChoiceBox<String> targetSnapshot;

    @FXML
    TreeTableView<CompareSnapshotRow> compareTree;

    @FXML
    TreeTableColumn<CompareSnapshotRow, String> sourceColumn;

    @FXML
    TreeTableColumn<CompareSnapshotRow, Boolean> diffColumn;

    @FXML
    TreeTableColumn<CompareSnapshotRow, String> targetColumn;

    @FXML
    AnchorPane mainPane;

    @FXML
    public void swapSnapshots(ActionEvent event) {

    }

    @FXML
    public void compare(ActionEvent event) {
        SnapshotDifference diff = compareSnapshotsHandler.compare(sourceSnapshot.getSelectionModel().getSelectedItem(),
                targetSnapshot.getSelectionModel().getSelectedItem());

        logger.info(diff.toString());

        CompareSnapshotRow snapshots = new CompareSnapshotRow(diff.getSourceObject().getName(), diff.isDifferent(),
                diff.getTargetObject().getName(), CompareObjectType.SNAPSHOT);
        TreeItem<CompareSnapshotRow> root = new TreeItem<>(snapshots);
        compareTree.setRoot(root);

        for (TableDifference td : diff.getTableDiffs()) {
            TreeItem<CompareSnapshotRow> tableRow = new TreeItem<>(new CompareSnapshotRow(
                    td.getSourceObject().getName(), td.isDifferent(), td.getTargetObject().getName(), CompareObjectType.TABLE));
            root.getChildren().add(tableRow);

            for (ColumnDifference cd : td.getColumnDiffs()) {
                TreeItem<CompareSnapshotRow> columnRow = new TreeItem<>(new CompareSnapshotRow(
                        cd.getSourceObject().getName(), cd.isDifferent(), cd.getTargetObject().getName(), CompareObjectType.COLUMN));
                tableRow.getChildren().add(columnRow);
            }
        }
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void generateScript(ActionEvent event) {

    }

    public void init() {
        ApricotSnapshot defSnapshot = snapshotManager.getDefaultSnapshot();

        List<ApricotSnapshot> snaps = snapshotManager.getAllSnapshots(projectManager.findCurrentProject());

        sourceSnapshot.getItems().clear();
        sourceSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));

        targetSnapshot.getItems().clear();
        targetSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        targetSnapshot.getSelectionModel().select(defSnapshot.getName());

        sourceColumn.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<CompareSnapshotRow, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<CompareSnapshotRow, String> param) {
                        TreeItem<CompareSnapshotRow> item = param.getValue();
                        return item.getValue().getSource();
                    }
                });

        diffColumn.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<CompareSnapshotRow, Boolean>, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(CellDataFeatures<CompareSnapshotRow, Boolean> param) {
                        TreeItem<CompareSnapshotRow> item = param.getValue();
                        return item.getValue().getDiff();
                    }
                });

        targetColumn.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<CompareSnapshotRow, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<CompareSnapshotRow, String> param) {
                        TreeItem<CompareSnapshotRow> item = param.getValue();
                        return item.getValue().getTarget();
                    }
                });
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
