package za.co.apricotdb.ui.handler;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.ui.RepositoryController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.repository.*;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import javax.annotation.Resource;
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

    @Resource
    ApplicationContext context;

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

    @ApricotErrorLogger(title = "Unable to create the Apricot Repository forms")
    public void showRepositoryForm() {
        if (!checkIfUrlConfigured()) {
            return;
        }

        try {
            localRepoService.initLocalRepo();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to initialize the local repository", ex);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-repository.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = null;
        try {
            window = loader.load();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Apricot Repository Import/Export");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("repository-small-s.png")));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);
        openProjectScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        // this extra thread was created to reflect the Progress Bar on the long running process
        new Thread(new Runnable() {
            @Override
            public void run() {
                RepositoryController controller = loader.<RepositoryController>getController();
                progressBarHandler.initProgressBar();
                RepositoryModel model = compareService.generateModel();
                if (model != null) {
                    controller.init(model);
                }
                progressBarHandler.finalizeProgressBar();

                Platform.runLater(() -> {
                    dialog.show();
                });
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
                progressBarHandler.initProgressBar();
                progressBarHandler.setProgress(0.1d);
                if (fullRefresh) {
                    localRepoService.refreshLocalRepo();
                }
                RepositoryModel model = compareService.generateModel();
                Platform.runLater(() -> {
                    repositoryController.init(model);
                });
                progressBarHandler.finalizeProgressBar();
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
    public void importRepoSnapshot(RepositoryRow row, String snapshotName) {

    }

    /**
     * Export the local project into the remote repository.
     */
    @ApricotErrorLogger(title = "Unable to export the Project into the Remote Repository")
    public void exportLocalProject(RepositoryRow row) {
        String projectName = row.getObjectName();
        String sProject = exportProcessor.serializeProject(row.getObjectName());
        String fileName = exportProcessor.getDefaultProjectExportFileName(projectName);
        File file = new File(LocalRepoService.LOCAL_REPO + "/" + fileName);
        try {
            FileUtils.write(file, sProject, Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        localRepoService.commitProjectFile(fileName, "The project export file \"" + fileName + "\" was added");
        remoteRepositoryService.pushRepository();
        refreshModel(false);

        Alert alert = alertDec.getAlert("Export Project", "The project \"" + projectName + "\" has been " +
                "successfully exported into the Remote Repository", Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    /**
     * Export the selected Snapshot into the Remote Repository.
     */
    public void exportLocalSnapshot(RepositoryRow row, String snapshotName) {

    }

    public void showRemoteProjectInfo(RepositoryRow row) {

    }

    public void deleteRemoteProject(RepositoryRow row) {
        if (!alertDec.requestYesNoOption("Delete Remote Project",
                "Do you want to delete the Project in the Remote Repository?",
                "Delete", Alert.AlertType.CONFIRMATION)) {
            return;
        }

        String fileName = row.getModelRow().getFile().getName();
        localRepoService.removeProjectFile(fileName, "The project \"" + fileName + "\" was deleted");
        remoteRepositoryService.pushRepository();
        refreshModel(false);

        Alert alert = alertDec.getAlert("Delete remote Project", "The project \"" + row.getObjectName() + "\" has been " +
                "successfully deleted in the Remote Repository", Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    public void showRemoteSnapshotInfo(RepositoryRow row) {

    }

    public void deleteRemoteSnapshot(RepositoryRow row) {

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
}
