package za.co.apricotdb.ui.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.ArrayList;
import java.util.List;

/**
 * This service copies the content of the source snapshot into the target snapshot.
 *
 * @author Anton Nazarov
 * @since 02/04/2021
 */
@Component
public class CopySnapshotService extends Service<Boolean> implements InitializableService {

    private static final Logger logger = LoggerFactory.getLogger(CopySnapshotService.class);

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ApricotTableRepository tableRepository;

    @Autowired
    ApricotRelationshipRepository relationshipRepository;

    private ObjectProperty<ApricotSnapshot> sourceSnapshotWrapper = new SimpleObjectProperty<>();

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                ApricotSnapshot targetSnapshot = snapshotManager.getDefaultSnapshot();
                ApricotSnapshot sourceSnapshot = sourceSnapshotWrapper.getValue();
                targetSnapshot.setTables(sourceSnapshot.getTables());
                int i = 0;
                List<ApricotRelationship> relationships = new ArrayList<>();
                for (ApricotTable table : sourceSnapshot.getTables()) {
                    updateMessage("Copying table: " + table.getName() + "...");
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

                    i++;
                    updateProgress(i, sourceSnapshot.getTables().size());
                }

                i = 0;
                for (ApricotRelationship relationship : relationships) {
                    updateMessage("Copying relationship: " + relationship.getName() + "...");
                    relationshipRepository.save(relationship);
                    i++;
                    updateProgress(i, relationships.size());
                }

                logger.info("CopySnapshotService: " + (System.currentTimeMillis() - start) + " ms");
                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);
    }

    public void setSourceSnapshot(ApricotSnapshot s) {
        sourceSnapshotWrapper.setValue(s);
    }
}
