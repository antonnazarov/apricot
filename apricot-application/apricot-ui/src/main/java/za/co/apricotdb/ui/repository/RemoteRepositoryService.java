package za.co.apricotdb.ui.repository;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.handler.RepositoryConfigHandler;
import za.co.apricotdb.ui.model.RepositoryConfiguration;
import za.co.apricotdb.ui.util.StringEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This component hold the functions to work with the remote repository.
 * 
 * @author Anton Nazarov
 * @since 07/04/2020
 */
@Component
public class RemoteRepositoryService {

    private static final Logger logger = LoggerFactory.getLogger(RemoteRepositoryService.class);

    @Autowired
    ProxyService proxyHandler;

    @Autowired
    RepositoryConfigHandler configHandler;

    public void checkRemoteRepository(RepositoryConfiguration config) throws ApricotRepositoryException {
        Map<String, Ref> refs = getRemoteReferences(config);
        if (refs == null || refs.isEmpty() || !refs.containsKey("refs/heads/master")) {
            throw new ApricotRepositoryException(
                    "The Master branch was not found in the Remote repository " + config.getRemoteUrl());
        }
    }

    public Map<String, Ref> getRemoteReferences(RepositoryConfiguration config) throws ApricotRepositoryException {
        Map<String, Ref> ret = new HashMap<>();

        proxyHandler.setProxy(config);
        boolean auth = StringUtils.isNotEmpty(config.getUserName()) && StringUtils.isNotEmpty(config.getPassword());

        try {
            if (auth) {
                ret = Git.lsRemoteRepository().setHeads(true).setTags(true).setRemote(config.getRemoteUrl())
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(config.getUserName(),
                                StringEncoder.decode(config.getPassword())))
                        .callAsMap();
            } else {
                ret = Git.lsRemoteRepository().setHeads(true).setTags(true).setRemote(config.getRemoteUrl())
                        .callAsMap();
            }
        } catch (Exception ex) {
            logger.error("The Repository Check has failed", ex);
            throw new ApricotRepositoryException(ex);
        }

        return ret;
    }

    public void cloneRepository() {
        RepositoryConfiguration config = configHandler.getRepositoryConfiguration();
        File localRepo = new File(LocalRepoService.LOCAL_REPO);
        proxyHandler.setProxy(config);
        boolean auth = StringUtils.isNotEmpty(config.getUserName()) && StringUtils.isNotEmpty(config.getPassword());
        try {
            Git result = null;
            if (auth) {
                result = Git.cloneRepository().setURI(config.getRemoteUrl()).setDirectory(localRepo)
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(config.getUserName(), StringEncoder.decode(config.getPassword())))
                        .call();
            } else {
                result = Git.cloneRepository().setURI(config.getRemoteUrl()).setDirectory(localRepo)
                        .call();
            }
            if (result != null) {
                logger.info("The remote repository was cloned: " + result.toString());
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to clone repository", ex);
        }
    }
}
