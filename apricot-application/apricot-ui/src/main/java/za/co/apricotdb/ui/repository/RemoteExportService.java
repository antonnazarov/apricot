package za.co.apricotdb.ui.repository;

import javafx.scene.control.Alert;
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
import za.co.apricotdb.ui.handler.ProgressBarHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

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
    AlertMessageDecorator alertDec;

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

    @Autowired
    ProgressBarHandler pbHandler;

    /**
     * Export the given snapshot into the remote repository.
     */
    @Transactional
    public void exportSnapshot(RepositoryRow row) {
        String projectName = row.getModelRow().getLocalProject().getName();

        if (!alertDec.requestYesNoOption("Export Snapshot",
                "The snapshot \"" + row.getObjectName() + "\" will be overridden by your local version of the snapshot",
                "Override", Alert.AlertType.CONFIRMATION)) {
            return;
        }

        pbHandler.initProgressBar();
        ApricotProject tmpProject = createTemporaryProject(row.getModelRow().getFile());
        // stage 1
        pbHandler.setProgress(0.25d);
        ApricotProject sourceProject = projectManager.getProjectByName(row.getModelRow().getLocalProject().getName());
        ApricotSnapshot localSnapshot = snapshotManager.getSnapshotByName(sourceProject, row.getObjectName());
        if (localSnapshot != null) {
            ApricotSnapshot clonedSnapshot = snapshotCloneManager.cloneSnapshot(localSnapshot.getName(), localSnapshot.getComment(), tmpProject, localSnapshot);
            tmpProject.getSnapshots().add(clonedSnapshot);
            projectManager.saveApricotProject(tmpProject);
        } else {
            throw new IllegalArgumentException("Unable to find the snapshot=[" + row.getObjectName() + "]");
        }

        // stage 2
        pbHandler.setProgress(0.5d);
        tmpProject = projectManager.getProjectByName(ProjectManager.UNDO_PROJECT_NAME);
        if (tmpProject != null) {
            // tmpProject.setName(projectName);
            // exportProject(tmpProject, row.getModelRow().getFile(), "The snapshot \"" + row.getObjectName() + "\" was exported into the project \"" + projectName + "\"");
            // stage 3
            pbHandler.setProgress(0.75d);
            projectManager.deleteProject(tmpProject);
            // stage 4
            pbHandler.setProgress(1.0d);
        } else {
            throw new IllegalArgumentException("Unable to find and export the temporary project=[" + ProjectManager.UNDO_PROJECT_NAME + "] during the export of the local snapshot to the Repository");
        }
        pbHandler.finalizeProgressBar();

        Alert alert = alertDec.getAlert("Export Snapshot", "The snapshot \"" + row.getObjectName() + "\" has been successfully exported", Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    /**
     * Export the given project into the remote repository.
     * Note: this export function add or updates (if exists) the project.
     */
    @Transactional
    public void exportProject(ApricotProject project, File file, String commitComment) {
        String sProject = exportProcessor.serializeProject(project);

        try {
            FileUtils.write(file, sProject, Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        localRepoService.commitProjectFile(file.getName(), commitComment);
        remoteRepositoryService.pushRepository();
    }

    /**
     * Merge the given snapshot into the project.
     */
    private ApricotProject createTemporaryProject(File file) {
        ApricotProject tmpProject = projectManager.getProjectByName(ProjectManager.UNDO_PROJECT_NAME);
        if (tmpProject != null) {
            projectManager.deleteProject(tmpProject);
        }

        try {
            return importProjectProcessor.importProject(FileUtils.readFileToString(file, Charset.defaultCharset()), true, ProjectManager.UNDO_PROJECT_NAME);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to import a temporary project into the local database", ex);
        }
    }
}
