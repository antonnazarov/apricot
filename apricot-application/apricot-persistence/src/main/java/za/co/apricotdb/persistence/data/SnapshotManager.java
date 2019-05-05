package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotColumnConstraintRepository;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;
import za.co.apricotdb.persistence.repository.ApricotSnapshotRepository;

/**
 * The SNAPSHOT- related operations.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
@Component
public class SnapshotManager {

    @Resource
    EntityManager em;

    @Resource
    ApricotSnapshotRepository snapshotRepository;

    @Resource
    ApricotColumnConstraintRepository columnConstraintRepository;

    @Resource
    ApricotRelationshipRepository relationshipRepository;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    ConstraintManager constraintManager;

    @Autowired
    RelationshipManager relationshipManager;

    public ApricotSnapshot getDefaultSnapshot(ApricotProject project) {
        ApricotSnapshot ret = null;

        TypedQuery<ApricotSnapshot> query = em.createNamedQuery("ApricotSnapshot.getDefaultSnapshot",
                ApricotSnapshot.class);
        query.setParameter("project", project);
        List<ApricotSnapshot> res = query.getResultList();
        if (res != null && res.size() > 0) {
            ret = res.get(0);
        }

        return ret;
    }

    public ApricotSnapshot getDefaultSnapshot() {
        ApricotProject project = projectManager.findCurrentProject();
        return getDefaultSnapshot(project);
    }

    public List<ApricotSnapshot> getAllSnapshots(ApricotProject project) {
        List<ApricotSnapshot> ret = new ArrayList<>();

        TypedQuery<ApricotSnapshot> query = em.createNamedQuery("ApricotSnapshot.getAllSnapshotsInProject",
                ApricotSnapshot.class);
        query.setParameter("project", project);
        List<ApricotSnapshot> res = query.getResultList();
        if (res != null && res.size() > 0) {
            ret = res;
        }

        return ret;
    }

    public ApricotSnapshot getSnapshotById(long id) {
        return snapshotRepository.getOne(id);
    }

    public ApricotSnapshot getSnapshotByName(ApricotProject project, String name) {
        ApricotSnapshot ret = null;
        TypedQuery<ApricotSnapshot> query = em.createNamedQuery("ApricotSnapshot.getSnapshotByName",
                ApricotSnapshot.class);
        query.setParameter("name", name);
        query.setParameter("project", project);
        List<ApricotSnapshot> res = query.getResultList();
        if (res != null && res.size() > 0) {
            ret = res.get(0);
        }

        return ret;
    }

    public ApricotSnapshot saveSnapshot(ApricotSnapshot snapshot) {
        return snapshotRepository.saveAndFlush(snapshot);
    }

    public void deleteSnapshot(ApricotSnapshot snapshot) {
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        for (ApricotTable t : tables) {
            List<ApricotColumnConstraint> cnstrs = constraintManager.getConstraintColumnsByTable(t);
            for (ApricotColumnConstraint cc : cnstrs) {
                columnConstraintRepository.delete(cc);
            }
        }

        List<ApricotRelationship> relationships = relationshipManager.getRelationshipsForTables(tables);
        for (ApricotRelationship r : relationships) {
            relationshipRepository.delete(r);
        }

        snapshotRepository.delete(snapshot);
    }

    public void setDefaultSnapshot(ApricotSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }

        List<ApricotSnapshot> snapshots = getAllSnapshots(snapshot.getProject());
        for (ApricotSnapshot s : snapshots) {
            s.setDefaultSnapshot(false);
            saveSnapshot(s);
        }

        snapshot.setDefaultSnapshot(true);
        saveSnapshot(snapshot);
    }
}
