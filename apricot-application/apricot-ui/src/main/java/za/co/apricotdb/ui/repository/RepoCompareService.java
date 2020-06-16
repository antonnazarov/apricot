package za.co.apricotdb.ui.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.comparator.SnapshotComparator;
import za.co.apricotdb.persistence.comparator.SnapshotDifference;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.support.export.ImportProjectProcessor;
import za.co.apricotdb.ui.handler.ProgressBarHandler;

import javax.transaction.Transactional;
import java.io.File;
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

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    SnapshotComparator snapshotComparator;

    @Autowired
    ExportProjectProcessor exportProcessor;

    @Autowired
    ImportProjectProcessor importProcessor;

    @Autowired
    ProgressBarHandler progressBarHandler;

    /**
     * Generate the Repository Model to show in the Repository Form.
     */
    @Transactional
    public RepositoryModel generateModel() {
        RepositoryModel model = null;
        try {
            ProjectItems items = localRepoService.readLocalRepo();
            logger.info("The following remote projects have been found in the repository: " + items.toString());

            model = compareLocalToRepo(items);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to read the content of the local repository", ex);
        }

        return model;
    }

    /**
     * Perform the comparison between the local projects and provided repo items.
     */
    private RepositoryModel compareLocalToRepo(ProjectItems repoItems) {
        RepositoryModel ret = new RepositoryModel();

        // read the whole list of the local projects into the detached entities,
        // using export/import via JSON
        List<ApricotProject> projects = projectManager.getAllProjects();
        List<ApricotProject> localProjects = getDetachedProjects(projects);

        List<ProjectItem> matchProjects = new ArrayList<>();
        // the cycle on the local list
        for (ApricotProject ap : localProjects) {
            ProjectItem pItem = repoItems.findProjectItem(ap.getName());
            if (pItem != null) {
                matchProjects.add(pItem);
            } else {
                //  export
                ModelRow mr = new ModelRow(RowType.PROJECT, false, ap.getName(), null);
                mr.setLocalProject(ap);
                ret.getRows().add(mr);
                logger.info("Added the export project to model: " + ap.getName());
            }
        }

        Map<String, ApricotProject> pm = getProjectMap(localProjects);
        for (ProjectItem pi : repoItems.getItems()) {
            ApricotProject ap = pm.get(pi.getProjectName());
            if (ap == null) {
                //  import
                ModelRow mr = new ModelRow(RowType.PROJECT, false, null, pi.getProjectName());
                ret.getRows().add(mr);
                mr.setRemoteProject(pi.getProject());
                mr.setFile(pi.getFile());
                logger.info("Added the import project to model: " + pi.getProjectName());
            }
        }

        // check the equality of the matched projects
        int total = matchProjects.size();
        int cnt = 0;
        for (ProjectItem pi : matchProjects) {
            List<ModelRow> snapshotRows = compareSnapshots(pm.get(pi.getProjectName()), pi.getProject(), pi.getFile());
            boolean eqSnaps = snapshotsEqual(snapshotRows);
            ModelRow mr = new ModelRow(RowType.PROJECT, eqSnaps, pi.getProjectName(), pi.getProjectName());
            mr.setLocalProject(pm.get(pi.getProjectName()));
            mr.setRemoteProject(pi.getProject());
            mr.setFile(pi.getFile());
            mr.getIncludedItems().addAll(snapshotRows);
            ret.getRows().add(mr);
            logger.info("Added the match project to model: " + pi.getProjectName() + ", equal=" + eqSnaps);

            cnt++;
            progressBarHandler.setProgress(0.8d + Double.valueOf(cnt) * 0.2d/Double.valueOf(total));
        }

        ret.sort();

        return ret;
    }

    @Transactional
    public ApricotProject getDetachedProject(ApricotProject p) {
        ApricotProject attached = projectManager.getProjectByName(p.getName());

        String sProj = exportProcessor.serializeProject(attached);
        return importProcessor.importProject(sProj, false);
    }

    @Transactional
    public List<ApricotProject> getDetachedProjects(List<ApricotProject> projects) {
        int total = projects.size();
        int cnt = 0;
        List<ApricotProject> ret = new ArrayList<>();
        for (ApricotProject p : projects) {
            ret.add(getDetachedProject(p));

            cnt++;
            progressBarHandler.setProgress(0.2d + Double.valueOf(cnt) * 0.6d/Double.valueOf(total));
        }

        return ret;
    }

    /**
     * Return true, if all snapshots in the list are equal.
     */
    private boolean snapshotsEqual(List<ModelRow> snapshotRows) {
        for (ModelRow r : snapshotRows) {
            if (!r.isEqual()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compare two projects with the same names.
     */
    private List<ModelRow> compareSnapshots(ApricotProject localProject, ApricotProject repoProject, File file) {
        List<ModelRow> rows = compareElements(getSnapshots(localProject, false), getSnapshots(repoProject, false),
                RowType.SNAPSHOT);

        // scan through the result and compare the matched snapshots
        for (ModelRow r : rows) {
            if (r.getLocalName() != null && r.getRemoteName() != null) {
                ApricotSnapshot localSnapshot = findSnapshot(localProject, r.getLocalName(), false);
                ApricotSnapshot repoSnapshot = findSnapshot(repoProject, r.getRemoteName(), false);
                if (localSnapshot != null && repoSnapshot != null) {
                    SnapshotDifference diff = snapshotComparator.compare(localSnapshot, repoSnapshot);
                    if (diff != null) {
                        r.setEqual(!diff.isDifferent());
                    }
                }
            }
            r.setLocalProject(localProject);
            r.setRemoteProject(repoProject);
            r.setFile(file);
        }

        return rows;
    }

    /**
     * Get the snapshot from the given project by the snapshot name.
     */
    @Transactional
    public ApricotSnapshot findSnapshot(ApricotProject p, String name, boolean fromDb) {
        List<ApricotSnapshot> snapshots = null;
        if (fromDb) {
            snapshots = snapshotManager.getSnapshotsForProject(p.getName());
        } else {
            snapshots = p.getSnapshots();
        }
        for (ApricotSnapshot s : snapshots) {
            if (s.getName().equals(name)) {
                return s;
            }
        }

        return null;
    }

    /**
     * Extract a list of names of the snapshots from the project.
     */
    private List<String> getSnapshots(ApricotProject project, boolean fromDb) {
        List<String> ret = new ArrayList<>();
        List<ApricotSnapshot> snapshots = null;
        if (fromDb) {
            snapshots = snapshotManager.getSnapshotsForProject(project.getName());
        } else {
            snapshots = project.getSnapshots();
        }

        if (snapshots != null) {
            snapshots.forEach(s -> {
                ret.add(s.getName());
            });
        }

        return ret;
    }

    private List<ModelRow> compareElements(List<String> list1, List<String> list2, RowType type) {
        List<ModelRow> ret = new ArrayList<>();

        List<String> rtn = new ArrayList<>(list1);
        rtn.retainAll(list2);
        //  the elements, which are presented in both lists
        for (String s : rtn) {
            ModelRow r = new ModelRow(type, false, s, s);
            ret.add(r);
            logger.info(type + "=[" + s + "]->matched");
        }
        List<String> cp = new ArrayList<>(list1);
        cp.removeAll(rtn);
        for (String s : cp) {
            ModelRow r = new ModelRow(type, false, s, null);
            ret.add(r);
            logger.info(type + "=[" + s + "]->export");
        }

        cp = new ArrayList<>(list2);
        cp.removeAll(rtn);
        for (String s : cp) {
            ModelRow r = new ModelRow(type, false, null, s);
            ret.add(r);
            logger.info(type + "=[" + s + "]->import");
        }

        return ret;
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
