package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.error.ApricotErrorHandler;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.repository.RemoteExportService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;

/**
 * Import the Snapshot from the Repository asynchronously.
 *
 * @author Anton Nazarov
 * @since 09/11/2020
 */
@Component
public class ImportRepoSnapshotService extends Service<Boolean> implements InitializableService {

    private static final Logger logger = LoggerFactory.getLogger(ImportRepoSnapshotService.class);

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    RemoteExportService remoteExportService;

    @Autowired
    RepositoryHandler repositoryHandler;

    @Autowired
    ApricotSnapshotHandler apricotSnapshotHandler;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @Autowired
    ApricotErrorHandler errorHandler;

    private File file;
    private String projectName;
    private String snapshotName;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                updateProgress(1, 6);
                updateMessage("Cleaning the temporary projects...");
                remoteExportService.removeTemporaryProject();

                updateProgress(2, 6);
                updateMessage("Creating a new temporary project...");
                remoteExportService.createTemporaryProject(file);

                updateProgress(3, 6);
                updateMessage("Remove the snapshot in the target project...");
                remoteExportService.removeSnapshotInTargetProject(projectName, snapshotName);

                updateProgress(4, 6);
                updateMessage("Clone the snapshot into the target project...");
                remoteExportService.cloneSnapshotIntoTargetProject(projectName, snapshotName);

                updateProgress(5, 6);
                updateMessage("Removing the temporary project...");
                remoteExportService.removeTemporaryProject();

                updateProgress(5, 6);
                updateMessage("Refreshing the data model...");
                repositoryHandler.refreshModel(false);

                logger.info("ImportRepoSnapshotService: " + (System.currentTimeMillis()-start) + " ms");

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);

        setOnSucceeded(e -> {
            Alert alert = alertDec.getAlert("Import Snapshot", "The snapshot \"" + snapshotName + "\" " +
                            "was successfully imported into the local project \"" + projectName + "\"",
                    Alert.AlertType.INFORMATION);
            alert.showAndWait();

            applicationInitializer.initializeDefault();
            // apricotSnapshotHandler.synchronizeSnapshot(true);
        });

        setOnFailed(e -> {
            errorHandler.showErrorInfo("Unable to import Snapshot", "Import Snapshot",
                    getException());
        });
    }

    public void setServiceData(File file, String projectName, String snapshotName) {
        this.file = file;
        this.projectName = projectName;
        this.snapshotName = snapshotName;
    }
}
