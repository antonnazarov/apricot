package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.error.ApricotErrorHandler;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.repository.LocalRepoService;
import za.co.apricotdb.ui.repository.RemoteRepositoryService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This service implements the dlete remote project functionality.
 *
 * @author Anton Nazarov
 * @since 09/11/2020
 */
@Component
public class DeleteRemoteProjectService  extends Service<Boolean> implements InitializableService {

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    RemoteRepositoryService remoteRepositoryService;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    RepositoryHandler repositoryHandler;

    @Autowired
    ApricotErrorHandler errorHandler;

    private String projectName;
    private String fileName;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                updateProgress(1, 3);
                updateMessage("Removing the project file...");
                localRepoService.removeProjectFile(fileName, "The project \"" + fileName + "\" was deleted");

                updateProgress(2, 3);
                updateMessage("Push the repository changes...");
                remoteRepositoryService.pushRepository();

                updateProgress(3, 3);
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
            Alert alert = alertDec.getAlert("Delete remote Project", "The project \"" + projectName + "\" has been " +
                    "successfully deleted in the Remote Repository", Alert.AlertType.INFORMATION);
            alert.showAndWait();
        });
        setOnFailed(e -> {
            errorHandler.showErrorInfo("Unable to delete Remote Project", "Delete Remote Project",
                    getException());
        });
    }

    public void initServiceData(String projectName, String fileName) {
        this.projectName = projectName;
        this.fileName = fileName;
    }
}
