package za.co.apricotdb.ui.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The local to repo comparison functionality is provided by this component.
 *
 * @author Anton Nazarov
 * @since 30/04/2020
 */
@Component
public class RepoCompareService {

    private final Logger logger = LoggerFactory.getLogger(RepoCompareService.class);

    @Autowired
    LocalRepoService localRepoService;

    @Autowired
    ProjectManager projectManager;

    /**
     * Generate the Repository Model to show in the Repository Form.
     */
    public RepositoryModel generateModel() {
        try {
            return compareLocalToRepo(localRepoService.readLocalRepo());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read the content of the local repository", ex);
        }
    }

    /**
     * Perform the comparison between the local projects and provided repo items.
     */
    private RepositoryModel compareLocalToRepo(ProjectItems repoItems) {
        RepositoryModel ret = new RepositoryModel();

        List<ApricotProject> projects = projectManager.getAllProjects();

        List<ProjectItem> matchProjects = new ArrayList<>();
        // the cycle on the local list
        for (ApricotProject ap : projects) {
            ProjectItem pItem = repoItems.findProjectItem(ap.getName());
            if (pItem != null) {
                matchProjects.add(pItem);
            } else {
                //  export
                ModelRow mr = new ModelRow(RowType.PROJECT, false, ap.getName(), null);
                ret.getRows().add(mr);
            }
        }

        Map<String, ApricotProject> pm = getProjectMap(projects);
        for (ProjectItem pi : repoItems.getItems()) {
            ApricotProject ap = pm.get(pi.getProjectName());
            if (ap == null) {
                //  import
                ModelRow mr = new ModelRow(RowType.PROJECT, false, null, pi.getProjectName());
                ret.getRows().add(mr);
            }
        }

        // check the equality of the matched projects
        for (ProjectItem pi : matchProjects) {
            ret.getRows().add(compareProjects(pm.get(pi.getProjectName()), pi.getProject()));
        }

        return ret;
    }

    private ModelRow compareProjects(ApricotProject localProj, ApricotProject repoProj) {
        return null;
    }

    /**
     * Read a list of the projects into the project map.
     */
    private Map<String, ApricotProject> getProjectMap(List<ApricotProject> prjs) {
        Map<String, ApricotProject> ret = new HashMap<>();

        prjs.forEach(p -> {
            ret.put(p.getName(), p);
        });

        return ret;
    }
}
