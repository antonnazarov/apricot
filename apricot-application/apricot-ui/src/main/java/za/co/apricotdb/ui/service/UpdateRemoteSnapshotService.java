package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.repository.RemoteExportService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;

/**
 * This service implements the update remote snapshot functionality.
 *
 * @author Anton Nazarov
 * @since 09/11/2020
 */
@Component
public class UpdateRemoteSnapshotService extends Service<Boolean> implements InitializableService {

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    RepositoryHandler repositoryHandler;

    @Autowired
    RemoteExportService remoteExportService;

    private String projectName;
    private String snapshotName;
    private File file;
    private String confirmationInfo;
    private boolean remove;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                updateProgress(1, 7);
                updateMessage("Cleaning the temporary projects...");
                remoteExportService.removeTemporaryProject();

                updateProgress(2, 7);
                updateMessage("Creating a new temporary project...");
                remoteExportService.createTemporaryProject(file);

                updateProgress(3, 7);
                updateMessage("Removing the target snapshot in the temporary project...");
                remoteExportService.removeTargetSnapshotInTemporaryProject(snapshotName);

                updateProgress(4, 7);
                updateMessage("Cloning the selected snapshot...");
                if (!remove) {
                    remoteExportService.cloneSnapshot(projectName, snapshotName);
                }

                updateProgress(5, 7);
                updateMessage("Exporting the project...");
                remoteExportService.exportProject(ProjectManager.TMP_PROJECT_NAME, file, confirmationInfo, projectName);

                updateProgress(6, 7);
                updateMessage("Removing the temporary project...");
                remoteExportService.removeTemporaryProject();

                updateProgress(7, 7);
                updateMessage("Refreshing the model...");
                repositoryHandler.refreshModel(false);

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);
        setOnSucceeded(e -> {
            Alert alert = alertDec.getAlert("Remote Snapshot", confirmationInfo, Alert.AlertType.INFORMATION);
            alert.showAndWait();
        });
        setOnFailed(e -> {
            throw new IllegalArgumentException(getException());
        });
    }

    public void initServiceData(String projectName, String snapshotName, File file, String confirmationInfo,
                                boolean remove) {
        this.projectName = projectName;
        this.snapshotName = snapshotName;
        this.file = file;
        this.confirmationInfo = confirmationInfo;
        this.remove = remove;
    }
}
