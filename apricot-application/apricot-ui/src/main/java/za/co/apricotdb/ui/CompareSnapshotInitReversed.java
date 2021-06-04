package za.co.apricotdb.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.comparator.CompareDiffColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareSourceColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareTargetColumnConstructor;
import za.co.apricotdb.ui.handler.CompareSnapshotsHandler;
import za.co.apricotdb.ui.handler.OverrideSnapshotHandler;

/**
 * Initializer of the Compare Snapshots form for the local and reversed snapshot comparison.
 *
 * @author Anton Nazarov
 * @since 29/03/2021
 */
@Component
public class CompareSnapshotInitReversed {

    @Autowired
    CompareSnapshotsHandler compareSnapshotsHandler;

    @Autowired
    CompareSourceColumnConstructor sourceColumnConstructor;

    @Autowired
    CompareTargetColumnConstructor targetColumnConstructor;

    @Autowired
    CompareDiffColumnConstructor diffColumnConstructor;

    @Autowired
    OverrideSnapshotHandler overrideSnapshotHandler;

    public void init(CompareSnapshotsController controller, ApricotSnapshot sourceSnapshot,
                                  ApricotSnapshot targetSnapshot) {
        String sourceName;
        String targetName;
        ApricotSnapshot reversedSnapshot;
        if (sourceSnapshot.getName().equals("Reversed Snapshot")) {
            reversedSnapshot = sourceSnapshot;
            sourceName = "Reversed Snapshot";
            targetName = targetSnapshot.getName();
            controller.sourceColumn.setText("Reverse Engineered");
            controller.targetColumn.setText("Local Snapshot");
        } else {
            reversedSnapshot = targetSnapshot;
            sourceName = sourceSnapshot.getName();
            targetName = "Reversed Snapshot";
            controller.sourceColumn.setText("Local Snapshot");
            controller.targetColumn.setText("Reverse Engineered");
        }

        controller.sourceSnapshot.getItems().clear();
        controller.sourceSnapshot.getItems().add(sourceName);
        controller.sourceSnapshot.getSelectionModel().select(sourceName);
        controller.sourceSnapshot.setDisable(true);

        controller.targetSnapshot.getItems().clear();
        controller.targetSnapshot.getItems().add(targetName);
        controller.targetSnapshot.getSelectionModel().select(targetName);
        controller.targetSnapshot.setDisable(true);

        controller.swapButton.setDisable(false);
        controller.compareButton.setDisable(true);


        // construct the three columns of the comparator table
        sourceColumnConstructor.construct(controller.sourceColumn);
        targetColumnConstructor.construct(controller.targetColumn);
        diffColumnConstructor.construct(controller.diffColumn);

        controller.root = compareSnapshotsHandler.compare(sourceSnapshot, targetSnapshot, controller.diffOnlyFlag.isSelected());
        controller.root.setExpanded(true);
        controller.compareTree.setRoot(controller.root);
        controller.compareTree.refresh();
        controller.compared = true;

        controller.generateScriptButton.setDisable(false);

        initSaveButton(controller, reversedSnapshot);

        controller.diffOnlyFlag.setOnAction(e -> {
            init(controller, sourceSnapshot, targetSnapshot);
        });

        //  redefine the swap button
        controller.swapButton.setOnAction(e -> {
            init(controller, targetSnapshot, sourceSnapshot);
        });
    }

    private void initSaveButton(CompareSnapshotsController controller, ApricotSnapshot reversedSnapshot) {
        Button saveButton = null;
        boolean wasInitialized = false;
        for (Node n : controller.buttonBar.getChildren()) {
            if (n.getId() != null && n.getId().equals("save_button")) {
                saveButton = (Button) n;
                wasInitialized = true;
                break;
            }
        }
        if (!wasInitialized) {
            saveButton = new Button();
            saveButton.setId("save_button");
            saveButton.setText("Save and Override");
            controller.buttonBar.getChildren().add(saveButton);
        }

        saveButton.setOnAction(e -> {
            //  save the reversed structure into the current Snapshot
            overrideSnapshotHandler.overrideSnapshotContent(reversedSnapshot);
            controller.cancel();
        });
    }
}
