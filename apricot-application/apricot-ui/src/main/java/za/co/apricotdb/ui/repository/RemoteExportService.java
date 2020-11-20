package za.co.apricotdb.ui.repository;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotCloneManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.support.export.ImportProjectProcessor;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This component allows to export a selected snapshot into the remote repository.
 *
 * @author Anton Nazarov
 * @since 09/06/2020
 */
@Component
public class RemoteExportService {

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ExportProjectProcessor exportProcessor;

    @Autowired
    RemoteRepositoryService remoteRepositoryService;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    ImportProjectProcessor importProjectProcessor;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotCloneManager snapshotCloneManager;

    /**
     * Export the given project into the remote repository.
     * Note: this export function add or updates (if exists) the project.
     */
    @Transactional
    public void exportProject(String projectName, File file, String commitComment, String projectRename) {
        ApricotProject project = projectManager.getProjectByName(projectName);
        String sProject = exportProcessor.serializeProject(project);

        if (projectRename != null) {
            sProject = sProject.replaceAll(projectName, projectRename);
        }

        try {
            FileUtils.write(file, sProject, Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        localRepoService.commitProjectFile(file.getName(), commitComment);
        remoteRepositoryService.pushRepository();
    }

    @Transactional
    public void removeTargetSnapshotInTemporaryProject(String snapshotName) {
        ApricotProject tmpProject = projectManager.getProjectByName(ProjectManager.TMP_PROJECT_NAME);
        if (tmpProject != null) {
            //  check if the snapshot exists on the remote side
            ApricotSnapshot oldSnapshot = snapshotManager.getSnapshotByName(tmpProject, snapshotName);
            if (oldSnapshot != null) {
                snapshotManager.deleteSnapshot(oldSnapshot);
            }
        }
    }

    @Transactional
    public void cloneSnapshot(String projectName, String snapshotName) {
        ApricotProject tmpProject = projectManager.getProjectByName(ProjectManager.TMP_PROJECT_NAME);
        if (tmpProject != null) {
            ApricotProject sourceProject = projectManager.getProjectByName(projectName);
            ApricotSnapshot localSnapshot = snapshotManager.getSnapshotByName(sourceProject, snapshotName);
            if (localSnapshot != null) {
                snapshotCloneManager.cloneSnapshot(localSnapshot.getName(), localSnapshot.getComment(), tmpProject, localSnapshot);
            } else {
                throw new IllegalArgumentException("Unable to find the snapshot=[" + snapshotName + "] in the project=[" + projectName + "]");
            }
        }
    }

    @Transactional
    public void cloneSnapshotIntoTargetProject(String projectName, String snapshotName) {
        ApricotProject tmpProject = projectManager.getProjectByName(ProjectManager.TMP_PROJECT_NAME);
        if (tmpProject != null) {
            ApricotSnapshot snapshot = snapshotManager.getSnapshotByName(tmpProject, snapshotName);
            if (snapshot != null) {
                ApricotProject targetProject = projectManager.getProjectByName(projectName);
                snapshotCloneManager.cloneSnapshot(snapshot.getName(), snapshot.getComment(), targetProject,
                        snapshot);

                //  set the imported snapshot as default
                ApricotSnapshot clonedSnapshot = snapshotManager.getSnapshotByName(targetProject, snapshotName);
                snapshotManager.setDefaultSnapshot(clonedSnapshot);
            } else {
                throw new IllegalArgumentException("Unable to find the snapshot=[" + snapshotName + "] in the " +
                        "remote project=[" + projectName + "]");
            }
        }
    }

    public void removeTemporaryProject() {
        ApricotProject tmpProject = projectManager.getProjectByName(ProjectManager.TMP_PROJECT_NAME);
        if (tmpProject != null) {
            projectManager.deleteProject(tmpProject);
        }
    }

    /**
     * Merge the given snapshot into the project.
     */
    public ApricotProject createTemporaryProject(File file) {
        try {
            return importProjectProcessor.importProject(FileUtils.readFileToString(file, Charset.defaultCharset()), true, ProjectManager.TMP_PROJECT_NAME);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to import a temporary project into the local database", ex);
        }
    }

    public void removeSnapshotInTargetProject(String projectName, String snapshotName) {
        ApricotProject project = projectManager.getProjectByName(projectName);
        if (project != null) {
            ApricotSnapshot snapshot = snapshotManager.getSnapshotByName(project, snapshotName);
            if (snapshot != null) {
                snapshotManager.deleteSnapshot(snapshot);
            }
        }
    }
}
