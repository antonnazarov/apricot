package za.co.apricotdb.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * This is a controller, which serves the apricot-compare-snapshots.fxml form.
 * 
 * @author Anton Nazarov
 * @since 15/10/2019
 */
@Component
public class CompareSnapshotsController {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @FXML
    ChoiceBox<String> sourceSnapshot;

    @FXML
    ChoiceBox<String> targetSnapshot;

    @FXML
    TreeTableView<String> compareTree;

    @FXML
    AnchorPane mainPane;

    @FXML
    public void swapSnapshots(ActionEvent event) {

    }

    @FXML
    public void compare(ActionEvent event) {

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
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
