package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;
import za.co.apricotdb.persistence.repository.ApricotSnapshotRepository;

@Component
public class SnapshotCloneManager {

    @Resource
    ApricotSnapshotRepository snapshotRepository;

    @Resource
    ApricotRelationshipRepository relationshipRepository;

    @Autowired
    TableCloneManager tableCloneManager;

    @Autowired
    RelationshipManager relationshipManager;

    @Autowired
    RelationshipCloneManager relationshipCloneManager;

    @Transactional
    public ApricotSnapshot cloneSnapshot(String name, String comment, ApricotProject project,
            ApricotSnapshot snapshot) {
        List<ApricotTable> clonedTables = new ArrayList<>();
        ApricotSnapshot clonedSnapshot = new ApricotSnapshot(name, new java.util.Date(), null, comment, false, project,
                clonedTables);

        // scan and clone the tables of the original snapshot
        for (ApricotTable table : snapshot.getTables()) {
            clonedTables.add(tableCloneManager.cloneTable(clonedSnapshot, table, true, false));
        }

        snapshotRepository.save(clonedSnapshot);

        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(snapshot.getTables());
        for (ApricotRelationship r : relationships) {
            ApricotRelationship clonedRelationship = relationshipCloneManager.cloneRelationship(r, clonedSnapshot);
            relationshipRepository.save(clonedRelationship);
        }

        return clonedSnapshot;
    }
}
