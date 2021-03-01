package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.handler.ApricotEntityHandler;
import za.co.apricotdb.ui.handler.ApricotSnapshotHandler;

import java.util.List;

/**
 * This asynchronous service is responsible for deleting the entities included into the given list.
 *
 * @author Anton Nazarov
 * @since 31/10/2020
 */
@Component
public class DeleteSelectedEntitiesService extends Service<Boolean> implements InitializableService {

    private final static Logger logger = LoggerFactory.getLogger(DeleteSelectedEntitiesService.class);

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    ApricotEntityHandler entityHandler;

    @Autowired
    ApricotSnapshotHandler snapshotHandler;

    private List<String> entities;

    public void init(List<String> entities) {
        this.entities = entities;
        reset();
        init("Delete Entities", "The deletion of " + entities.size() + " entities is in progress");

        setOnSucceeded(e -> {
            snapshotHandler.synchronizeSnapshot(true);
        });

        setOnFailed(e -> {
            logger.error("Unable to delete Entity(s)", getException());
            throw new IllegalArgumentException(getException());
        });
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                int cnt = 0;
                for (String e : entities) {
                    entityHandler.deleteEntity(e);
                    updateProgress(cnt, entities.size());
                    updateMessage("Removing Entity: " + e);
                    cnt++;
                }

                logger.info("DeleteSelectedEntitiesService: " + (System.currentTimeMillis()-start) + " ms");

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        progressInitializer.init(title, headerText, this);
    }
}
