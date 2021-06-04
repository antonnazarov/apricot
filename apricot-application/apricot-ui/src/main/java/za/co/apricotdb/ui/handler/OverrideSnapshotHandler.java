package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.service.CopySnapshotService;
import za.co.apricotdb.ui.service.DeleteSelectedEntitiesService;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This component overrides the whole content of the current snapshot by the given (unmanaged) Snapshot.
 *
 * @author Anton Nazarov
 * @since 30/03/2021
 */
@Component
public class OverrideSnapshotHandler {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    DeleteSelectedEntitiesService deleteSelectedEntitiesService;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    @Autowired
    CopySnapshotService copySnapshotService;

    /**
     * Fully override the snapshot content.
     */
    @Transactional
    public void overrideSnapshotContent(ApricotSnapshot snapshot) {
        ApricotSnapshot targetSnapshot = snapshotManager.getDefaultSnapshot();
        deleteSelectedEntitiesService.init(targetSnapshot.getTablesAsString());
        deleteSelectedEntitiesService.setOnSucceeded(e -> {
            saveSnapshotData(targetSnapshot, snapshot);
        });
        deleteSelectedEntitiesService.start();
    }

    private void saveSnapshotData(ApricotSnapshot targetSnapshot, ApricotSnapshot snapshot) {
        copySnapshotService.init("Save Snapshot data", "Saving of the Snapshot data is in progress...");
        copySnapshotService.setSourceSnapshot(snapshot);
        copySnapshotService.setOnSucceeded(e -> {
            snapshotHandler.synchronizeSnapshot(true);
            saveSnapshotComment(snapshot.getComment());
        });
        copySnapshotService.start();
    }

    private void saveSnapshotComment(String comment) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("The content of this snapshot was reverse engineered and overridden the original on ")
                .append(df.format(new java.util.Date())).append(" with the following parameters: \n")
                .append(comment);
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        snapshot.setComment(sb.toString());
        snapshot.setUpdated(new java.util.Date());

        snapshotManager.saveSnapshot(snapshot);
    }
}
