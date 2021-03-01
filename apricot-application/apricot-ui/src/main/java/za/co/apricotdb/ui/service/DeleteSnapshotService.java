package za.co.apricotdb.ui.service;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotColumnConstraintRepository;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;
import za.co.apricotdb.persistence.repository.ApricotSnapshotRepository;

import javax.annotation.Resource;
import java.util.List;

/**
 * This asynchronous service contains the Progress Bar observable version of the "Delete Snapshot" operation.
 *
 * @author Anton Nazarov
 * @since 28/08/2020
 */
@Component
public class DeleteSnapshotService extends Service<Boolean> implements InitializableService {

    private static final Logger logger = LoggerFactory.getLogger(DeleteSnapshotService.class);

    @Autowired
    TableManager tableManager;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Resource
    ApricotColumnConstraintRepository columnConstraintRepository;

    @Resource
    ApricotRelationshipRepository relationshipRepository;

    @Resource
    ApricotSnapshotRepository snapshotRepository;

    @Autowired
    ProgressInitializer progressInitializer;

    private LongProperty snapshotId = new SimpleLongProperty();

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId.setValue(snapshotId);
    }

    public Long getSnapshotId() {
        return this.snapshotId.getValue();
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                ApricotSnapshot snapshot = snapshotManager.findSnapshotById(getSnapshotId());
                String snapshotName = snapshot.getName();
                List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
                List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);
                int total = tables.size() + relationships.size() + 10;
                int cnt = 0;

                for (ApricotTable t : tables) {
                    List<ApricotColumnConstraint> constraints = constraintManager.getConstraintColumnsByTable(t);
                    for (ApricotColumnConstraint cc : constraints) {
                        columnConstraintRepository.delete(cc);
                    }

                    cnt++;
                    updateProgress(cnt, total);
                    updateMessage("Removing constraints for entity: " + t.getName());
                }

                for (ApricotRelationship r : relationships) {
                    String relationshipName = r.getParent().getName() + "->" + r.getChild().getName();
                    relationshipRepository.delete(r);

                    cnt++;
                    updateProgress(cnt, total);
                    updateMessage("Removing relationship: " + relationshipName);
                }

                snapshotRepository.delete(snapshot);
                updateProgress(total, total);
                updateMessage("All done: the Snapshot " + snapshotName + " has been removed");

                logger.info("DeleteSnapshotService: " + (System.currentTimeMillis()-start) + " ms");

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        progressInitializer.init(title, headerText, this);
    }
}
