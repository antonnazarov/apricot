package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

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

    public ApricotSnapshot addUndoSnapshot(ApricotSnapshot snapshot) {
        ApricotProject undoProject = getUndoProject();
        List<ApricotSnapshot> snapshots = undoProject.getSnapshots();
        snapshots.sort((s1, s2) -> {
            return (int) (s1.getId() - s2.getId());
        });

        while (snapshots.size() >= UNDO_SNAPSHOTS_NUM) {
            snapshots.remove(0);
        }

        ApricotSnapshot clone = cloneManager.cloneSnapshot(snapshot.getName(), snapshot.getComment(), undoProject,
                snapshot);
        return snapshotManager.saveSnapshot(clone);
    }
}
