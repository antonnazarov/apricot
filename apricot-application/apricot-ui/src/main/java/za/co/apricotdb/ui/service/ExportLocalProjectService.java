package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.repository.LocalRepoService;
import za.co.apricotdb.ui.repository.RemoteRepositoryService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This service exports the local project into the repository.
 *
 * @author Anton Nazarov
 * @since 19/11/2020
 */
@Component
public class ExportLocalProjectService extends Service<Boolean> implements InitializableService {

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    RepositoryHandler repositoryHandler;

    @Autowired
    ExportProjectProcessor exportProcessor;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    RemoteRepositoryService remoteRepositoryService;

    private String projectName;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                updateProgress(1, 5);
                updateMessage("Serializing the current project...");
                String sProject = exportProcessor.serializeProject(projectName);

                updateProgress(2, 5);
                updateMessage("Writing the project file...");
                String fileName = exportProcessor.getDefaultProjectExportFileName(projectName);
                File file = new File(LocalRepoService.LOCAL_REPO + "/" + fileName);
                try {
                    FileUtils.write(file, sProject, Charset.defaultCharset());
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }

                updateProgress(3, 5);
                updateMessage("Committing the project file...");
                localRepoService.commitProjectFile(fileName, "The project export file \"" + fileName + "\" was added");

                updateProgress(4, 5);
                updateMessage("Performing the repository 'push' command...");
                remoteRepositoryService.pushRepository();

                updateProgress(5, 5);
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
            Alert alert = alertDec.getAlert("Export Project", "The project \"" + projectName + "\" has been " +
                    "successfully exported into the Remote Repository", Alert.AlertType.INFORMATION);
            alert.showAndWait();
        });
        setOnFailed(e -> {
            throw new IllegalArgumentException(getException());
        });
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
