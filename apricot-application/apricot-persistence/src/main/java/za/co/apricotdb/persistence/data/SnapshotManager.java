package za.co.apricotdb.persistence.data;

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

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
        } else {
            // the default snapshot was not found. Fix it and set the default snapshot
            ret = setDefaultSnapshot();
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

    @Transactional
    public List<ApricotSnapshot> getSnapshotsForProject(String projectName) {
        List<ApricotSnapshot> ret = new ArrayList<>();

        TypedQuery<ApricotSnapshot> query = em.createNamedQuery("ApricotSnapshot.getSnapshotsByProjectName",
                ApricotSnapshot.class);
        query.setParameter("projectName", projectName);
        List<ApricotSnapshot> res = query.getResultList();
        if (res != null && res.size() > 0) {
            ret = res;
        }

        return ret;
    }

    @Transactional
    public ApricotSnapshot getSnapshotById(long id) {
        return snapshotRepository.getOne(id);
    }

    public ApricotSnapshot findSnapshotById(long id) {
        return snapshotRepository.findById(id).get();
    }

    @Transactional
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

    @Transactional
    public void deleteSnapshot(ApricotSnapshot snapshot) {
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        for (ApricotTable t : tables) {
            List<ApricotColumnConstraint> constraints = constraintManager.getConstraintColumnsByTable(t);
            for (ApricotColumnConstraint cc : constraints) {
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

    /**
     * Set the default snapshot if the one was not set. It sets the first snapshot in the list.
     */
    public ApricotSnapshot setDefaultSnapshot() {
        ApricotSnapshot ret = null;
        List<ApricotSnapshot> snapshots = getAllSnapshots(projectManager.findCurrentProject());
        if (snapshots != null && snapshots.size() > 0) {
            ret = snapshots.get(0);
            setDefaultSnapshot(ret);
        }

        return ret;
    }

    /**
     * Find the snapshot by the given name in the given apricot project.
     */
    public ApricotSnapshot findSnapshotByName(ApricotProject project, String snapshotName) {
        for (ApricotSnapshot s : project.getSnapshots()) {
            if (s.getName().equals(snapshotName)) {
                return s;
            }
        }

        return null;
    }
}
