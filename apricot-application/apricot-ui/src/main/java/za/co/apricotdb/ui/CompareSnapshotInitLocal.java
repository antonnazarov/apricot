package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.comparator.CompareDiffColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareSourceColumnConstructor;
import za.co.apricotdb.ui.comparator.CompareTargetColumnConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Initializer of the compare snapshot form for the local snapshots comparison.
 *
 * @author Anton Nazarov
 * @since 21/03/2021
 */
@Component
public class CompareSnapshotInitLocal {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    CompareSourceColumnConstructor sourceColumnConstructor;

    @Autowired
    CompareTargetColumnConstructor targetColumnConstructor;

    @Autowired
    CompareDiffColumnConstructor diffColumnConstructor;

    public void init(CompareSnapshotsController controller) {
        ApricotSnapshot defSnapshot = snapshotManager.getDefaultSnapshot();

        List<ApricotSnapshot> snaps = snapshotManager.getAllSnapshots(projectManager.findCurrentProject());
        ApricotSnapshot srcSnap = controller.findSourceSnapshot(snaps, defSnapshot);

        controller.sourceSnapshot.getItems().clear();
        controller.sourceSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        controller.sourceSnapshot.getSelectionModel().select(srcSnap.getName());

        controller.targetSnapshot.getItems().clear();
        controller.targetSnapshot.getItems().addAll(snaps.stream().map(ApricotSnapshot::getName).collect(Collectors.toList()));
        controller.targetSnapshot.getSelectionModel().select(defSnapshot.getName());

        // construct the three columns of the comparator table
        sourceColumnConstructor.construct(controller.sourceColumn);
        targetColumnConstructor.construct(controller.targetColumn);
        diffColumnConstructor.construct(controller.diffColumn);

        controller.sourceSnapshot.setOnAction(e -> {
            if (controller.compared) {
                controller.compare(e);
            }
        });

        controller.targetSnapshot.setOnAction(e -> {
            if (controller.compared) {
                controller.compare(e);
            }
        });

        controller.diffOnlyFlag.setOnAction(e -> {
            if (controller.compared) {
                controller.compare(e);
            }
        });
    }
}
