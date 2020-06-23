package za.co.apricotdb.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.comparator.CompareDiffColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.comparator.CompareSourceColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareTargetColumnConstructor;
import za.co.apricotdb.ui.handler.CompareScriptHandler;
import za.co.apricotdb.ui.handler.CompareSnapshotsHandler;
import za.co.apricotdb.ui.handler.NonTransactionalPort;

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

    @Autowired
    CompareSourceColumnConstructor sourceColumnConstructor;

    @Autowired
    CompareTargetColumnConstructor targetColumnConstructor;

    @Autowired
    CompareDiffColumnConstructor diffColumnConstructor;

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

    private TreeItem<CompareSnapshotRow> root;
    private boolean compared = false;
    private boolean compareRemote;

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
    public void cancel(ActionEvent event) {
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

    /**
     * Do the normal init between snapshots of the current project.
     */
    public void init() {
        ApricotSnapshot defSnapshot = snapshotManager.getDefaultSnapshot();

        List<ApricotSnapshot> snaps = snapshotManager.getAllSnapshots(projectManager.findCurrentProject());
        ApricotSnapshot srcSnap = findSourceSnapshot(snaps, defSnapshot);

        sourceSnapshot.getItems().clear();
        sourceSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        sourceSnapshot.getSelectionModel().select(srcSnap.getName());

        targetSnapshot.getItems().clear();
        targetSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        targetSnapshot.getSelectionModel().select(defSnapshot.getName());

        // construct the three columns of the comparator table
        sourceColumnConstructor.construct(sourceColumn);
        targetColumnConstructor.construct(targetColumn);
        diffColumnConstructor.construct(diffColumn);

        sourceSnapshot.setOnAction(e -> {
            if (compared) {
                compare(e);
            }
        });

        targetSnapshot.setOnAction(e -> {
            if (compared) {
                compare(e);
            }
        });

        diffOnlyFlag.setOnAction(e -> {
            if (compared) {
                compare(e);
            }
        });
    }

    /**
     * Initialize the form for the comparison of the local and remote snapshots
     */
    public void initCompareRemote(ApricotSnapshot localSnapshot, ApricotSnapshot remoteSnapshot) {
        this.compareRemote = true;

        String localName = localSnapshot.getName() + " (local)";
        sourceSnapshot.getItems().clear();
        sourceSnapshot.getItems().add(localName);
        sourceSnapshot.getSelectionModel().select(localName);
        sourceSnapshot.setDisable(true);

        String repoName = remoteSnapshot.getName() + " (repository)";
        targetSnapshot.getItems().clear();
        targetSnapshot.getItems().add(repoName);
        targetSnapshot.getSelectionModel().select(repoName);
        targetSnapshot.setDisable(true);

        swapButton.setDisable(true);
        compareButton.setDisable(true);

        sourceColumn.setText("Local Snapshot");
        targetColumn.setText("Repository Snapshot");

        // construct the three columns of the comparator table
        sourceColumnConstructor.construct(sourceColumn);
        targetColumnConstructor.construct(targetColumn);
        diffColumnConstructor.construct(diffColumn);

        root = compareSnapshotsHandler.compare(localSnapshot, remoteSnapshot, diffOnlyFlag.isSelected());
        root.setExpanded(true);
        compareTree.setRoot(root);
        compareTree.refresh();
        compared = true;

        generateScriptButton.setDisable(false);

        diffOnlyFlag.setOnAction(e -> {
            initCompareRemote(localSnapshot, remoteSnapshot);
        });
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    /**
     * Find the snapshot to initialize the source snapshot slop.
     */
    private ApricotSnapshot findSourceSnapshot(List<ApricotSnapshot> snaps, ApricotSnapshot defSnapshot) {
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
