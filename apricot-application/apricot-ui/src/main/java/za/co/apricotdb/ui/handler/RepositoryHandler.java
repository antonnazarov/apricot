package za.co.apricotdb.ui.handler;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.ui.RepositoryController;
import za.co.apricotdb.ui.error.ApricotErrorHandler;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.repository.ApricotRepositoryException;
import za.co.apricotdb.ui.repository.LocalRepoService;
import za.co.apricotdb.ui.repository.RemoteExportService;
import za.co.apricotdb.ui.repository.RemoteRepositoryService;
import za.co.apricotdb.ui.repository.RepoCompareService;
import za.co.apricotdb.ui.repository.RepositoryConsistencyService;
import za.co.apricotdb.ui.repository.RepositoryModel;
import za.co.apricotdb.ui.repository.RepositoryRow;
import za.co.apricotdb.ui.repository.RepositoryRowFactory;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * The Repository related functionality is supported by this component.
 *
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryHandler {

    private final Logger logger = LoggerFactory.getLogger(RepositoryHandler.class);

    @Autowired
    RepositoryRowFactory rowFactory;

    @Autowired
    RepositoryConsistencyService consistencyService;

    @Autowired
    RepositoryConfigHandler configHandler;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    RemoteRepositoryService remoteRepositoryService;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    RepoCompareService compareService;

    @Autowired
    ProgressBarHandler progressBarHandler;

    @Autowired
    RepositoryController repositoryController;

    @Autowired
    ImportProjectHandler importProjectHandler;

    @Autowired
    ExportProjectProcessor exportProcessor;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    RemoteExportService remoteExportService;

    @Autowired
    ApricotErrorHandler errorHandler;

    @Autowired
    CompareSnapshotsHandler compareSnapshotsHandler;

    @Autowired
    HtmlProjectInfoHandler projectHandler;

    @Autowired
    HtmlSnapshotInfoHandler snapshotHandler;

    @Autowired
    HtmlViewHandler htmlViewHandler;

    @Autowired
    DialogFormHandler formHandler;

    @ApricotErrorLogger(title = "Unable to create the Apricot Repository form")
    public void showRepositoryForm() {
        if (!checkIfUrlConfigured()) {
            return;
        }

        try {
            localRepoService.initLocalRepo();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to initialize the local repository", ex);
        }

        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-repository.fxml",
                "repository-small-s.png", "Apricot Repository Import/Export");

        // this extra thread was created to reflect the Progress Bar on the long running process
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RepositoryController controller = form.getController();
                    progressBarHandler.initProgressBar();
                    RepositoryModel model = compareService.generateModel();
                    if (model != null) {
                        controller.init(model);
                    }

                    Platform.runLater(() -> {
                        form.show();
                    });
                } catch (Exception ex) {
                    errorHandler.showErrorInfo("Unable to create the Apricot Repository form", "Apricot " +
                            "Repository", ex);
                } finally {
                    progressBarHandler.finalizeProgressBar();
                }
            }
        }).start();
    }

    /**
     * The full refresh means to wipe the local repo and clone from the remote repository.
     */
    @ApricotErrorLogger(title = "Unable to refresh the Repository state")
    public void refreshModel(boolean fullRefresh) {
        if (fullRefresh && !checkRemoteRepository()) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    progressBarHandler.initProgressBar();
                    progressBarHandler.setProgress(0.1d);

                    if (fullRefresh) {
                        localRepoService.refreshLocalRepo();
                    }
                    RepositoryModel model = compareService.generateModel();
                    Platform.runLater(() -> {
                        repositoryController.init(model);
                    });
                } catch (Exception ex) {
                    errorHandler.showErrorInfo("Unable to refresh the Repository state", "Refresh State", ex);
                } finally {
                    progressBarHandler.finalizeProgressBar();
                }
            }
        }).start();
    }

    /**
     * Import the project into the local Apricot.
     */
    public void importRepoProject(RepositoryRow row) {
        File f = row.getModelRow().getFile();
        if (f != null && alertDec.requestYesNoOption("Import Repo File",
                "Do you want to import the project " + row.getObjectName() + "?", "Import",
                Alert.AlertType.CONFIRMATION)) {
            importProjectHandler.importProject(f);
            refreshModel(false);
        }
    }

    /**
     * Import the selected snapshot into the local Apricot.
     */
    public void importRepoSnapshot(RepositoryRow row) {
        String projectName = row.getModelRow().getLocalProject().getName();
        boolean isOverride = (row.getModelRow().getRemoteName() != null && row.getModelRow().getRemoteName().equals(row.getModelRow().getLocalName()));
        if (isOverride && !alertDec.requestYesNoOption("Import Snapshot",
                "The local version of snapshot \"" + row.getObjectName() + "\" will be overridden by the one from the" +
                        " Remote Repository",
                "Override", Alert.AlertType.CONFIRMATION)) {
            return;
        } else if (!isOverride && !alertDec.requestYesNoOption("Import Snapshot",
                "The snapshot \"" + row.getObjectName() + "\" will be imported into the project \"" + projectName + "\"",
                "Import", Alert.AlertType.CONFIRMATION)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBarHandler.initProgressBar();
                try {
                    remoteExportService.removeTemporaryProject();
                    progressBarHandler.setProgress(0.1d);

                    remoteExportService.createTemporaryProject(row.getModelRow().getFile());
                    progressBarHandler.setProgress(0.3d);

                    remoteExportService.removeSnapshotInTargetProject(projectName, row.getObjectName());
                    progressBarHandler.setProgress(0.4d);

                    remoteExportService.cloneSnapshotIntoTargetProject(projectName, row.getObjectName());
                    progressBarHandler.setProgress(0.9d);

                    remoteExportService.removeTemporaryProject();
                    progressBarHandler.setProgress(1.0d);

                    Platform.runLater(() -> {
                        refreshModel(false);
                        Alert alert = alertDec.getAlert("Import Snapshot", "The snapshot \"" + row.getObjectName() + "\" " +
                                        "was successfully imported into the local project \"" + projectName + "\"",
                                Alert.AlertType.INFORMATION);
                        alert.showAndWait();
                    });
                } catch (Exception ex) {
                    errorHandler.showErrorInfo("Unable to import the snapshot from the repository",
                            "Import Snapshot", ex);
                } finally {
                    progressBarHandler.finalizeProgressBar();
                }
            }
        }).start();
    }

    /**
     * Export the local project into the remote repository.
     */
    @ApricotErrorLogger(title = "Unable to export the Project into the Remote Repository")
    public void exportLocalProject(RepositoryRow row) {
        if (!alertDec.requestYesNoOption("Export Project",
                "Do you want to export the local version of the project into the remote repository?",
                "Export", Alert.AlertType.CONFIRMATION)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBarHandler.initProgressBar();
                try {
                    String projectName = row.getObjectName();
                    String sProject = exportProcessor.serializeProject(row.getObjectName());
                    progressBarHandler.setProgress(0.2d);

                    String fileName = exportProcessor.getDefaultProjectExportFileName(projectName);
                    File file = new File(LocalRepoService.LOCAL_REPO + "/" + fileName);
                    try {
                        FileUtils.write(file, sProject, Charset.defaultCharset());
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                    progressBarHandler.setProgress(0.5d);

                    localRepoService.commitProjectFile(fileName, "The project export file \"" + fileName + "\" was added");
                    progressBarHandler.setProgress(0.7d);

                    remoteRepositoryService.pushRepository();
                    progressBarHandler.setProgress(0.9d);

                    refreshModel(false);
                    progressBarHandler.setProgress(1.0d);

                    Platform.runLater(() -> {
                        Alert alert = alertDec.getAlert("Export Project", "The project \"" + projectName + "\" has been " +
                                "successfully exported into the Remote Repository", Alert.AlertType.INFORMATION);
                        alert.showAndWait();
                    });
                } catch (Exception ex) {
                    errorHandler.showErrorInfo("Unable to export the Project into the Remote Repository",
                            "Export Project", ex);
                } finally {
                    progressBarHandler.finalizeProgressBar();
                }
            }
        }).start();
    }

    /**
     * Export the selected Snapshot into the Remote Repository.
     */
    @ApricotErrorLogger(title = "Unable to export the local Snapshot into the Remote Repository")
    public void exportLocalSnapshot(RepositoryRow row) {
        String projectName = row.getModelRow().getLocalProject().getName();
        boolean isOverride = (row.getModelRow().getRemoteName() != null && row.getModelRow().getRemoteName().equals(row.getModelRow().getLocalName()));
        if (isOverride && !alertDec.requestYesNoOption("Export Snapshot",
                "The snapshot \"" + row.getObjectName() + "\" will be overridden by your local version of the snapshot",
                "Override", Alert.AlertType.CONFIRMATION)) {
            return;
        } else if (!isOverride && !alertDec.requestYesNoOption("Export Snapshot",
                "The snapshot \"" + row.getObjectName() + "\" will be exported into the project \"" + projectName + "\" of the Remote Repository",
                "Export", Alert.AlertType.CONFIRMATION)) {
            return;
        }

        updateRemoteSnapshot(projectName, row.getObjectName(), row.getModelRow().getFile(),
                "The snapshot \"" + row.getObjectName() + "\" of the project \"" + projectName + "\" was exported",
                false);
    }

    public void showRemoteProjectInfo(RepositoryRow row) {
        htmlViewHandler.showHtmlViewForm(projectHandler.getProjectValueMap(row.getModelRow().getRemoteProject()),
                "repository-project-info.html", "Project Info");
    }

    @ApricotErrorLogger(title = "Unable to delete Project in the Remote Repository")
    public void deleteRemoteProject(RepositoryRow row) {
        if (!alertDec.requestYesNoOption("Delete Remote Project",
                "Do you want to delete the Project in the Remote Repository?",
                "Delete", Alert.AlertType.WARNING)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBarHandler.initProgressBar();
                try {

                    String fileName = row.getModelRow().getFile().getName();
                    localRepoService.removeProjectFile(fileName, "The project \"" + fileName + "\" was deleted");
                    progressBarHandler.setProgress(0.3d);

                    remoteRepositoryService.pushRepository();
                    progressBarHandler.setProgress(0.8d);

                    refreshModel(false);
                    progressBarHandler.setProgress(1.0d);

                    Platform.runLater(() -> {
                        Alert alert = alertDec.getAlert("Delete remote Project", "The project \"" + row.getObjectName() + "\" has been " +
                                "successfully deleted in the Remote Repository", Alert.AlertType.INFORMATION);
                        alert.showAndWait();
                    });
                } catch (Exception ex) {
                    errorHandler.showErrorInfo("Unable to delete the Project in the Remote Repository",
                            "Delete Project", ex);
                } finally {
                    progressBarHandler.finalizeProgressBar();
                }
            }
        }).start();
    }

    public void showRemoteSnapshotInfo(RepositoryRow row) {
        htmlViewHandler.showHtmlViewForm(snapshotHandler.getSnapshotValueMap(row.getModelRow().getRemoteSnapshot()),
                "repository-snapshot-info.html", "Snapshot Info");
    }

    /**
     * Delete the snapshot in the Remote Repository.
     */
    @ApricotErrorLogger(title = "Unable to delete Snapshot in the Remote Repository")
    public void deleteRemoteSnapshot(RepositoryRow row) {
        String projectName = row.getModelRow().getLocalProject().getName();

        if (!alertDec.requestYesNoOption("Remove Remote Snapshot",
                "The snapshot \"" + row.getObjectName() + "\" will be removed from the Remote Repository",
                "Remove", Alert.AlertType.WARNING)) {
            return;
        }

        updateRemoteSnapshot(projectName, row.getObjectName(), row.getModelRow().getFile(),
                "The snapshot \"" + row.getObjectName() + "\" of the project \"" + projectName + "\" was deleted",
                true);
    }

    private void updateRemoteSnapshot(String projectName, String snapshotName, File file, String confirmationInfo,
                                      boolean remove) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                progressBarHandler.initProgressBar();
                try {
                    remoteExportService.removeTemporaryProject();
                    progressBarHandler.setProgress(0.1d);

                    remoteExportService.createTemporaryProject(file);
                    progressBarHandler.setProgress(0.3d);

                    remoteExportService.removeTargetSnapshotInTemporaryProject(snapshotName);
                    progressBarHandler.setProgress(0.4d);

                    if (!remove) {
                        remoteExportService.cloneSnapshot(projectName, snapshotName);
                        progressBarHandler.setProgress(0.5d);
                    }

                    remoteExportService.exportProject(ProjectManager.TMP_PROJECT_NAME, file, confirmationInfo, projectName);
                    progressBarHandler.setProgress(0.8d);

                    remoteExportService.removeTemporaryProject();
                    progressBarHandler.setProgress(1.0d);
                } catch (Exception ex) {
                    errorHandler.showErrorInfo("Unable to update the remote snapshot information",
                            "Update Snapshot", ex);
                } finally {
                    progressBarHandler.finalizeProgressBar();
                }

                Platform.runLater(() -> {
                    refreshModel(false);
                    Alert alert = alertDec.getAlert("Remote Snapshot", confirmationInfo, Alert.AlertType.INFORMATION);
                    alert.showAndWait();
                });
            }
        }).start();
    }

    /**
     * Check if the Remote Repository if properly configured.
     */
    private boolean checkIfUrlConfigured() {
        boolean first = true;
        if (!consistencyService.isRemoteRepositoryUrlConfigured()) {
            while (true) {
                if (first) {
                    first = false;
                } else {
                    if (!alertDec.requestYesNoOption("Configure Remote Git Repository",
                            "The URL of the remote Git repository is not configured",
                            "Configure", Alert.AlertType.WARNING)) {
                        return false;
                    }
                }
                configHandler.showRepositoryConfigForm(true);
                if (consistencyService.isRemoteRepositoryUrlConfigured()) {
                    break;
                }
            }
        }

        return true;
    }

    /**
     * Check the presence of the remote repository.
     * Prompt the config form if the check did not pass.
     */
    public boolean checkRemoteRepository() {
        while (true) {
            try {
                remoteRepositoryService.checkRemoteRepository(configHandler.getRepositoryConfiguration());
                return true;
            } catch (ApricotRepositoryException ex) {
                if (!alertDec.requestYesNoOption("The Remote Repository Check",
                        "Unable to access the Remote Repository: " + ex.getMessage(),
                        "Configure", Alert.AlertType.WARNING)) {
                    return false;
                } else {
                    configHandler.showRepositoryConfigForm(true);
                }
            }
        }
    }

    public void showSnapshotDifferences(RepositoryRow row) {
        compareSnapshotsHandler.openCompareSnapshotsForm(true, row.getModelRow());
    }
}
