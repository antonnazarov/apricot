package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;
import za.co.apricotdb.persistence.repository.ApricotTableRepository;
import za.co.apricotdb.ui.service.DeleteSelectedEntitiesService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    ApricotTableRepository tableRepository;

    @Resource
    ApricotRelationshipRepository relationshipRepository;

    /**
     * Fully override the snapshot content.
     */
    @Transactional
    public void overrideSnapshotContent(ApricotSnapshot snapshot) {
        ApricotSnapshot targetSnapshot = snapshotManager.getDefaultSnapshot();
        deleteSelectedEntitiesService.init(targetSnapshot.getTablesAsString());
        deleteSelectedEntitiesService.setOnSucceeded(e -> {
            saveSnapshotData(targetSnapshot, snapshot);
            snapshotHandler.synchronizeSnapshot(true);
        });
        deleteSelectedEntitiesService.start();
    }

    private void saveSnapshotData(ApricotSnapshot targetSnapshot, ApricotSnapshot snapshot) {
        targetSnapshot.setTables(snapshot.getTables());
        List<ApricotRelationship> relationships = new ArrayList<>();
        for (ApricotTable table : snapshot.getTables()) {
            table.setSnapshot(targetSnapshot);
            tableRepository.save(table);

            for (ApricotConstraint constraint : table.getConstraints()) {
                if (constraint.getType() == ConstraintType.FOREIGN_KEY) {
                    ApricotRelationship relationship = constraint.getRelationship();
                    if (relationship != null) {
                        relationships.add(relationship);
                    } else {
                        throw new IllegalArgumentException("The Relationship was not found for the FK constraint: " + constraint.getName());
                    }
                }
            }
        }

        for (ApricotRelationship relationship : relationships) {
            relationshipRepository.save(relationship);
        }

        targetSnapshot.setComment(snapshot.getComment());
        targetSnapshot.setUpdated(new java.util.Date());
        //  snapshotManager.saveSnapshot(targetSnapshot);
    }
}
