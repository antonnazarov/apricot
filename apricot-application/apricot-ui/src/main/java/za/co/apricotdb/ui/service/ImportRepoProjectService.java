package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ImportProjectHandler;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;

/**
 * This asynch service imports the project from Repository into the local Apricot.
 *
 * @author Anton Nazarov
 * @since 09/11/2020
 */
@Component
public class ImportRepoProjectService extends Service<Boolean> implements InitializableService {

    private static final Logger logger = LoggerFactory.getLogger(ImportRepoProjectService.class);

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    ImportProjectHandler importProjectHandler;

    @Autowired
    RepositoryHandler repositoryHandler;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ApplicationInitializer applicationInitializer;

    private File file;
    private String projectName;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                updateProgress(1, 5);
                updateMessage("Importing the project data...");
                importProjectHandler.importProject(file);

                updateProgress(4, 5);
                updateMessage("Refreshing the data model...");
                repositoryHandler.refreshModel(false);

                logger.info("ImportRepoProjectService: " + (System.currentTimeMillis()-start) + " ms");

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);
        setOnSucceeded(e -> {
            applicationInitializer.initializeDefault();
            Alert alert = alertDecorator.getAlert("Import Project",
                    "The project \"" + projectName + "\" was successfully imported", Alert.AlertType.INFORMATION);
            alert.showAndWait();
        });

        setOnFailed(e -> {
            throw new IllegalArgumentException(getException());
        });
    }

    public void setServiceData(File file, String projectName) {
        this.file = file;
        this.projectName = projectName;
    }
}
