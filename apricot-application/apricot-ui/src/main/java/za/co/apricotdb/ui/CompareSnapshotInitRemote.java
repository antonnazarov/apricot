package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.comparator.CompareDiffColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareSourceColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareTargetColumnConstructor;
import za.co.apricotdb.ui.handler.CompareSnapshotsHandler;

/**
 * Initializer of the Compare Snapshots form for the local and remote snapshot comparison.
 *
 * @author Anton Nazarov
 * @since 21/03/2021
 */
@Component
public class CompareSnapshotInitRemote {

    @Autowired
    CompareSnapshotsHandler compareSnapshotsHandler;

    @Autowired
    CompareSourceColumnConstructor sourceColumnConstructor;

    @Autowired
    CompareTargetColumnConstructor targetColumnConstructor;

    @Autowired
    CompareDiffColumnConstructor diffColumnConstructor;

    public void init(CompareSnapshotsController controller, ApricotSnapshot localSnapshot,
                                  ApricotSnapshot remoteSnapshot) {
        String localName = localSnapshot.getName() + " (local)";
        controller.sourceSnapshot.getItems().clear();
        controller.sourceSnapshot.getItems().add(localName);
        controller.sourceSnapshot.getSelectionModel().select(localName);
        controller.sourceSnapshot.setDisable(true);

        String repoName = remoteSnapshot.getName() + " (repository)";
        controller.targetSnapshot.getItems().clear();
        controller.targetSnapshot.getItems().add(repoName);
        controller.targetSnapshot.getSelectionModel().select(repoName);
        controller.targetSnapshot.setDisable(true);

        controller.swapButton.setDisable(true);
        controller.compareButton.setDisable(true);

        controller.sourceColumn.setText("Local Snapshot");
        controller.targetColumn.setText("Repository Snapshot");

        // construct the three columns of the comparator table
        sourceColumnConstructor.construct(controller.sourceColumn);
        targetColumnConstructor.construct(controller.targetColumn);
        diffColumnConstructor.construct(controller.diffColumn);

        controller.root = compareSnapshotsHandler.compare(localSnapshot, remoteSnapshot, controller.diffOnlyFlag.isSelected());
        controller.root.setExpanded(true);
        controller.compareTree.setRoot(controller.root);
        controller.compareTree.refresh();
        controller.compared = true;

        controller.generateScriptButton.setDisable(false);

        controller.diffOnlyFlag.setOnAction(e -> {
            init(controller, localSnapshot, remoteSnapshot);
        });
    }
}
