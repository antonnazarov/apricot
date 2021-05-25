package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.RepositoryController;
import za.co.apricotdb.ui.error.ApricotErrorHandler;
import za.co.apricotdb.ui.repository.LocalRepoService;
import za.co.apricotdb.ui.repository.RepoCompareService;
import za.co.apricotdb.ui.repository.RepositoryModel;

/**
 * This service implements the full refresh of the repository model.
 *
 * @author Anton Nazarov
 * @since 10/11/2020
 */
@Component
public class RefreshRepoModelService extends Service<Boolean> implements InitializableService {

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    RepoCompareService compareService;

    @Autowired
    RepositoryController repositoryController;

    @Autowired
    ApricotErrorHandler errorHandler;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                updateProgress(1, 4);
                updateMessage("Refreshing the local repository...");
                localRepoService.refreshLocalRepo();

                updateProgress(2, 4);
                updateMessage("Generating the model...");
                RepositoryModel model = compareService.generateModel();

                updateProgress(3, 4);
                updateMessage("Initializing the model...");
                repositoryController.init(model);

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);
        setOnFailed(e -> {
            errorHandler.showErrorInfo("Unable to refresh the Repository data", "Refresh Repository",
                    getException());
        });
    }
}
