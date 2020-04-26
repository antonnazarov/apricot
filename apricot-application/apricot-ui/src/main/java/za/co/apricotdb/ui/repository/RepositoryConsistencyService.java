package za.co.apricotdb.ui.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.handler.RepositoryConfigHandler;
import za.co.apricotdb.ui.model.RepositoryConfiguration;

/**
 * This component helps to do checks of the consistency of the Repository
 * configuration.
 * 
 * @author Anton Nazarov
 * @since 26/04/2020
 */
@Component
public class RepositoryConsistencyService {

    @Autowired
    RepositoryConfigHandler configHandler;

    /**
     * Check if the local repository catalogue exists.
     */
    public boolean localRepoExists() {
        Path path = FileSystems.getDefault().getPath(LocalRepoService.LOCAL_REPO);
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            return true;
        }

        return false;
    }

    /**
     * Check if the URL of the remote repository is present in the configuration and
     * valid.
     */
    public boolean isRemoteRepositoryUrlConfigured() {
        RepositoryConfiguration config = configHandler.getRepositoryConfiguration();

        if (StringUtils.isNotEmpty(config.getRemoteUrl())) {
            UrlValidator urlValidator = new UrlValidator();

            return urlValidator.isValid(config.getRemoteUrl());
        }

        return false;
    }
    
    /**
     * Check if the local repository is consistent Git- repo.
     */
    public boolean isLocalRepositoryConsistent() {
        if (!localRepoExists()) {
            return false; 
        }
        
        if (RepositoryCache.FileKey.isGitRepository(new File(LocalRepoService.LOCAL_REPO), FS.DETECTED)) {
            return true;
        }
        
        return false;
    }

    public void createLocalRepo() throws IOException {
        FileUtils.forceMkdir(new File(LocalRepoService.LOCAL_REPO));
    }

    public void clearLocalRepo() throws IOException {
        FileUtils.cleanDirectory(new File(LocalRepoService.LOCAL_REPO));
    }
}
