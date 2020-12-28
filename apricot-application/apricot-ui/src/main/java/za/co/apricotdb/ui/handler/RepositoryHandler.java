package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.RepositoryController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.repository.ApricotRepositoryException;
import za.co.apricotdb.ui.repository.LocalRepoService;
import za.co.apricotdb.ui.repository.RemoteRepositoryService;
import za.co.apricotdb.ui.repository.RepoCompareService;
import za.co.apricotdb.ui.repository.RepositoryConsistencyService;
import za.co.apricotdb.ui.repository.RepositoryModel;
import za.co.apricotdb.ui.repository.RepositoryRow;
import za.co.apricotdb.ui.service.DeleteRemoteProjectService;
import za.co.apricotdb.ui.service.ExportLocalProjectService;
import za.co.apricotdb.ui.service.ImportRepoProjectService;
import za.co.apricotdb.ui.service.ImportRepoSnapshotService;
import za.co.apricotdb.ui.service.RefreshRepoModelService;
import za.co.apricotdb.ui.service.ShowRepositoryFormService;
import za.co.apricotdb.ui.service.UpdateRemoteSnapshotService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;

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
    RepositoryController repositoryController;

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

    @Autowired
    ShowRepositoryFormService showRepositoryFormService;

    @Autowired
    ImportRepoProjectService importRepoProjectService;

    @Autowired
    ImportRepoSnapshotService importRepoSnapshotService;

    @Autowired
    ExportLocalProjectService exportLocalProjectService;

    @Autowired
    UpdateRemoteSnapshotService updateRemoteSnapshotService;

    @Autowired
    DeleteRemoteProjectService deleteRemoteProjectService;

    @Autowired
    RefreshRepoModelService refreshRepoModelService;

    @ApricotErrorLogger(title = "Unable to create the Apricot Repository form")
    public void showRepositoryForm() {
        if (!checkIfUrlConfigured()) {
            return;
        }

        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-repository.fxml",
                "repository-small-s.png", "Apricot Repository Import/Export");

        showRepositoryFormService.setApricotForm(form);
        showRepositoryFormService.init("The Repository Form", "preparing the form...");
        showRepositoryFormService.start();
    }

    /**
     * The full refresh means to wipe the local repo and clone from the remote repository.
     */
    @ApricotErrorLogger(title = "Unable to refresh the Repository state")
    public void refreshModel(boolean fullRefresh) {
        if (fullRefresh && !checkRemoteRepository()) {
            return;
        }

        if (fullRefresh) {
            localRepoService.refreshLocalRepo();
        }

        RepositoryModel model = compareService.generateModel();
        repositoryController.init(model);
    }

    @ApricotErrorLogger(title = "Unable to refresh the Repository state")
    public void refreshModel() {
        if (!checkRemoteRepository()) {
            return;
        }

        refreshRepoModelService.init("Refresh the repository", "The refresh- operation is in progress");
        refreshRepoModelService.start();
    }

    /**
     * Import the project into the local Apricot.
     */
    public void importRepoProject(RepositoryRow row) {
        File f = row.getModelRow().getFile();
        if (f != null && alertDec.requestYesNoOption("Import Repo File",
                "Do you want to import the project " + row.getObjectName() + "?", "Import",
                Alert.AlertType.CONFIRMATION)) {
            importRepoProjectService.init("Import Project", "The Apricot Project is being imported from the repository");
            importRepoProjectService.setServiceData(f, row.getObjectName());
            importRepoProjectService.start();
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

        importRepoSnapshotService.init("Import Snapshot", "The snapshot is being imported from the repository...");
        importRepoSnapshotService.setServiceData(row.getModelRow().getFile(), projectName, row.getObjectName());
        importRepoSnapshotService.start();
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

        exportLocalProjectService.init("Export Project", "The local Apricot project is being exported...");
        exportLocalProjectService.setProjectName(row.getObjectName());
        exportLocalProjectService.start();
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

        deleteRemoteProjectService.init("Delete Remote Project", "The deletion of the repository project is in progress...");
        deleteRemoteProjectService.initServiceData(row.getObjectName(), row.getModelRow().getFile().getName());
        deleteRemoteProjectService.start();
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
        updateRemoteSnapshotService.init("Update Remote Snapshot", "The Remote snapshot update is in progress...");
        updateRemoteSnapshotService.initServiceData(projectName, snapshotName, file, confirmationInfo, remove);
        updateRemoteSnapshotService.start();
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
