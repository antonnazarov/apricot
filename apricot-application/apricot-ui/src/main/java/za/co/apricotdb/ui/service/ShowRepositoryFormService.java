package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.RepositoryController;
import za.co.apricotdb.ui.error.ApricotErrorHandler;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.repository.LocalRepoService;
import za.co.apricotdb.ui.repository.RepoCompareService;
import za.co.apricotdb.ui.repository.RepositoryModel;

import java.io.IOException;

/**
 * This service creates and shows the form of Remote Repository.
 *
 * @author Anton Nazarov
 * @since 07/11/2020
 */
@Component
public class ShowRepositoryFormService extends Service<Boolean> implements InitializableService {

    private static final Logger logger = LoggerFactory.getLogger(ShowRepositoryFormService.class);

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    RepoCompareService compareService;

    @Autowired
    ApricotErrorHandler errorHandler;

    private ApricotForm form;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                updateProgress(1, 5);
                updateMessage("Initializing the local repository...");
                try {
                    localRepoService.initLocalRepo();
                } catch (IOException ex) {
                    throw new IllegalArgumentException("Unable to initialize the local repository", ex);
                }

                updateProgress(3, 5);
                updateMessage("Generating the model...");
                RepositoryModel model = compareService.generateModel();

                updateProgress(4, 5);
                updateMessage("Initializing the form controller...");
                if (model != null) {
                    RepositoryController controller = form.getController();
                    controller.init(model);
                }

                updateProgress(5, 5);
                updateMessage("All done...");

                logger.info("ShowRepositoryFormService: " + (System.currentTimeMillis()-start) + " ms");

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);
        setOnSucceeded(e -> {
            form.show();
        });
        setOnFailed(e -> {
            errorHandler.showErrorInfo("Unable to show Repository", "Initialize Repository",
                    getException());
        });
    }

    public void setApricotForm(ApricotForm form) {
        this.form = form;
    }
}
