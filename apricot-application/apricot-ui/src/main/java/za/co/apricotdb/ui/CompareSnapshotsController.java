package za.co.apricotdb.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.handler.CompareScriptHandler;
import za.co.apricotdb.ui.handler.NonTransactionalPort;

import java.util.List;

/**
 * This is a controller, which serves the apricot-compare-snapshots.fxml form.
 * 
 * @author Anton Nazarov
 * @since 15/10/2019
 */
@Component
public class CompareSnapshotsController {

    @Autowired
    CompareScriptHandler compareScriptHandler;

    @Autowired
    NonTransactionalPort port;

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
    CheckBox diffOnlyFlag;

    @FXML
    Button generateScriptButton;

    @FXML
    Button swapButton;

    @FXML
    Button compareButton;

    @FXML
    HBox buttonBar;

    TreeItem<CompareSnapshotRow> root;
    boolean compared = false;

    @FXML
    public void swapSnapshots(ActionEvent event) {
        String source = sourceSnapshot.getSelectionModel().getSelectedItem();
        String target = targetSnapshot.getSelectionModel().getSelectedItem();
        sourceSnapshot.getSelectionModel().select(target);
        targetSnapshot.getSelectionModel().select(source);

        if (compared) {
            compare(null);
        }
    }

    /**
     * Perform the comparison of the selected snapshots.
     */
    @FXML
    public void compare(ActionEvent event) {
        root = port.compare(sourceSnapshot.getSelectionModel().getSelectedItem(),
                targetSnapshot.getSelectionModel().getSelectedItem(), diffOnlyFlag.isSelected());
        root.setExpanded(true);
        compareTree.setRoot(root);
        compareTree.refresh();
        compared = true;
        generateScriptButton.setDisable(false);
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    /**
     * Generate the script for the selected differences alignment.
     */
    @FXML
    public void generateScript(ActionEvent event) {
        if (compared && root != null) {
            compareScriptHandler.generateScript(root);
        }
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    /**
     * Find the snapshot to initialize the source snapshot slop.
     */
    ApricotSnapshot findSourceSnapshot(List<ApricotSnapshot> snaps, ApricotSnapshot defSnapshot) {
        ApricotSnapshot ret = null;
        int idx = snaps.indexOf(defSnapshot);
        if (idx == 0) {
            ret = snaps.get(idx + 1);
        } else {
            ret = snaps.get(idx - 1);
        }

        return ret;
    }
}
