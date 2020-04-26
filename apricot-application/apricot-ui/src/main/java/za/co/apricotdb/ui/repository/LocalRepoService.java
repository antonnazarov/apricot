package za.co.apricotdb.ui.repository;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.support.export.ImportProjectProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

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
     * Read content of the local repository.
     */
    public ProjectItems readLocalRepo() throws IOException {
        ProjectItems ret = new ProjectItems();
        File localRepo = new File(LOCAL_REPO);

        Iterator<File> itr = FileUtils.iterateFiles(localRepo, new String[]{"txt"}, false);
        while (itr.hasNext()) {
            File f = itr.next();
            if (isApricotProjectFile(f)) {
                try {
                    ApricotProject project = importProjectProcessor.importProject(FileUtils.readFileToString(f,
                            Charset.defaultCharset()), false);
                    ret.add(new ProjectItem(project.getName(), f.getName(), project));
                } catch (Exception ex) {
                    //  upparseable project file
                }
            }
        }

        return ret;
    }
}
