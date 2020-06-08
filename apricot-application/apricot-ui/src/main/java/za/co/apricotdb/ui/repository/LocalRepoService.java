package za.co.apricotdb.ui.repository;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.support.export.ImportProjectProcessor;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.handler.ProgressBarHandler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the local repo functionality service.
 *
 * @author Anton Nazarov
 * @since 26/04/2020
 */
@Component
public class LocalRepoService {

    @Autowired
    RepositoryConsistencyService consistencyService;

    @Autowired
    RemoteRepositoryService remoteRepositoryService;

    @Autowired
    ImportProjectProcessor importProjectProcessor;

    @Autowired
    ProgressBarHandler progressBarHandler;

    public static final String LOCAL_REPO = System.getProperty("user.home") + "/.apricotdb/repository";

    /**
     * Check if the given file is a valid Apricot project export file.
     */
    public boolean isApricotProjectFile(File file) throws IOException {
        String sFile = FileUtils.readFileToString(file, Charset.defaultCharset());

        return sFile.contains("\"id\":") && sFile.contains("\"name\":") && sFile.contains("\"targetDatabase\":")
                && sFile.contains("\"current\":") && sFile.contains("\"created\":")
                && sFile.contains("\"erdNotation\":") && sFile.contains("\"snapshots\": [")
                && sFile.contains(ExportProjectProcessor.PROJECT_DIVIDER);
    }

    /**
     * Initialize the local repository cloning the remote one.
     * This operation has been performed only if the local
     * repository does not exist or inconsistent.
     */
    public void initLocalRepo() throws IOException {
        if (!consistencyService.isLocalRepositoryConsistent()) {
            if (!consistencyService.localRepoExists()) {
                consistencyService.createLocalRepo();
            } else {
                consistencyService.clearLocalRepo();
            }
            remoteRepositoryService.cloneRepository();
        }
    }

    /**
     * Remove the local repo and create it from scratch, cloning the remote Repository.
     */
    @ApricotErrorLogger(title = "Unable to refresh the local repository")
    public void refreshLocalRepo() {
        try {
            if (!consistencyService.localRepoExists()) {
                consistencyService.createLocalRepo();
            }
            consistencyService.clearLocalRepo();
            remoteRepositoryService.cloneRepository();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to refresh the local repository", ex);
        }
    }

    /**
     * Read content of the local repository.
     */
    public ProjectItems readLocalRepo() throws IOException {
        ProjectItems ret = new ProjectItems();
        File localRepo = new File(LOCAL_REPO);

        int total = getFileList(LOCAL_REPO).size();
        int cnt = 0;
        Iterator<File> itr = FileUtils.iterateFiles(localRepo, new String[]{"txt"}, false);
        while (itr.hasNext()) {
            File f = itr.next();
            if (isApricotProjectFile(f)) {
                try {
                    ApricotProject project = importProjectProcessor.importProject(FileUtils.readFileToString(f,
                            Charset.defaultCharset()), false);
                    ret.add(new ProjectItem(project.getName(), f, project));
                } catch (Exception ex) {
                    //  unparseable project file
                    ex.printStackTrace();
                }
            }

            cnt++;
            progressBarHandler.setProgress(Double.valueOf(cnt) * 0.2d / Double.valueOf(total));
        }

        return ret;
    }

    /**
     * Add the given file to the local repository.
     */
    public void commitProjectFile(String fileName, String comment) {
        Git git = null;
        try {
            git = Git.open(new File(LOCAL_REPO));
            git.add().addFilepattern(fileName).call();
            git.commit().setMessage(comment).call();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to commit the added file " + fileName, e);
        } finally {
            git.close();
        }
    }

    public void removeProjectFile(String fileName, String comment) {
        Git git = null;
        try {
            git = Git.open(new File(LOCAL_REPO));
            git.rm().addFilepattern(fileName).call();
            git.commit().setMessage(comment).call();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to commit the deleted file " + fileName, e);
        } finally {
            git.close();
        }
    }

    private List<String> getFileList(String path) {
        List<String> result = new ArrayList<>();
        File folder = new File(path);
        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                if (!f.isDirectory()) {
                    result.add(f.toString());
                }
            }
        }

        return result;
    }
}
