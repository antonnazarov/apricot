package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;

/**
 * This undo manager is responsible for registering undo data in the undo-
 * project.
 * 
 * @author Anton Nazarov
 * @since 29/04/2019
 */
@Component
public class UndoSnapshotManager {

    public static final int UNDO_SNAPSHOTS_NUM = 10;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;
    
    @Autowired
    SnapshotCloneManager cloneManager;

    public ApricotProject getUndoProject() {
        ApricotProject undoProject = projectManager.getProjectByName(ProjectManager.UNDO_PROJECT_NAME);
        if (undoProject == null) {
            undoProject = new ApricotProject(ProjectManager.UNDO_PROJECT_NAME, "The invisible UNDO project", "N/A",
                    false, new java.util.Date(), new ArrayList<ApricotSnapshot>(),
                    new ArrayList<ApricotProjectParameter>(), new ArrayList<ApricotView>());
            projectManager.saveApricotProject(undoProject);
        }

        return undoProject;
    }

    public void cleanUndoProject() {
        ApricotProject p = getUndoProject();

        for (ApricotSnapshot s : p.getSnapshots()) {
            snapshotManager.deleteSnapshot(s);
        }
    }

    public ApricotSnapshot buildUndoSnapshot(ApricotSnapshot snapshot) {
        ApricotProject undoProject = getUndoProject();
        List<ApricotSnapshot> snapshots = undoProject.getSnapshots();
        snapshots.sort((s1, s2) -> {
            return (int) (s1.getId() - s2.getId());
        });

        while (snapshots.size() >= UNDO_SNAPSHOTS_NUM) {
            snapshotManager.deleteSnapshot(snapshots.get(0));
            snapshots.remove(0);
        }

        ApricotSnapshot clone = cloneManager.cloneSnapshot(
                snapshot.getName() + " (" + RandomStringUtils.randomAlphanumeric(8) + ")", snapshot.getComment(),
                undoProject, snapshot);
        snapshotManager.saveSnapshot(clone);
        snapshotManager.setDefaultSnapshot(clone);
        
        return clone;
    }

    public void undoCurrentSnapshot(ApricotSnapshot undoSnapshot) {
        ApricotProject project = projectManager.findCurrentProject();
        ApricotSnapshot currSnapshot = snapshotManager.getDefaultSnapshot();

        undoSnapshot.setName(currSnapshot.getName());
        undoSnapshot.setComment(currSnapshot.getComment());
        undoSnapshot.setUpdated(new java.util.Date());
        undoSnapshot.setProject(project);
        snapshotManager.deleteSnapshot(currSnapshot);

        undoSnapshot.setDefaultSnapshot(true);
        snapshotManager.saveSnapshot(undoSnapshot);
    }
}
