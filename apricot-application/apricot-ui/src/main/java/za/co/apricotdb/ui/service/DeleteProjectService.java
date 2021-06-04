package za.co.apricotdb.ui.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.repository.ApricotProjectRepository;
import za.co.apricotdb.ui.error.ApricotErrorHandler;

import javax.annotation.Resource;
import java.util.List;

/**
 * This service deletes the selected project, showing the progress bar.
 *
 * @author Anton Nazarov
 * @since 19/11/2020
 */
@Component
public class DeleteProjectService extends Service<Boolean> implements InitializableService {

    private static final Logger logger = LoggerFactory.getLogger(DeleteProjectService.class);

    @Resource
    private ApricotProjectRepository projectRepository;

    @Autowired
    ProgressInitializer progressInitializer;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ViewManager viewManager;

    @Autowired
    ObjectLayoutManager objectLayoutManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ApricotErrorHandler errorHandler;

    private ApricotProject project;

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                long start = System.currentTimeMillis();

                project = projectManager.getProjectByName(project.getName());

                List<ApricotSnapshot> snapshots = snapshotManager.getAllSnapshots(project);
                List<ApricotView> views = viewManager.getAllViews(project);
                int cnt = 0;
                int total = snapshots.size() + views.size();


                for (ApricotSnapshot s : snapshots) {
                    snapshotManager.deleteSnapshot(s);
                    cnt++;
                    updateProgress(cnt, total);
                    updateMessage("Removing the snapshot: " + s.getName());
                }

                for (ApricotView v : views) {
                    List<ApricotObjectLayout> layouts = objectLayoutManager.getObjectLayoutsByView(v);
                    for (ApricotObjectLayout l : layouts) {
                        objectLayoutManager.deleteObjectLayout(l);
                        updateMessage("Removing the layout: " + l.getObjectName());
                    }

                    cnt++;
                    updateProgress(cnt, total);
                    updateMessage("Removing the view " + v.getName());
                }

                updateProgress(total, total);
                updateMessage("Removing the project");
                projectRepository.delete(project);

                logger.info("DeleteProjectService: " + (System.currentTimeMillis()-start) + " ms");

                return true;
            }
        };
    }

    @Override
    public void init(String title, String headerText) {
        reset();
        progressInitializer.init(title, headerText, this);
        setOnSucceeded(e -> {
        });
        setOnFailed(e -> {
            errorHandler.showErrorInfo("Unable to delete Project", "Delete Project",
                    getException());
        });
    }

    public void initServiceData(ApricotProject project) {
        this.project = project;
    }
}
