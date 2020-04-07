package za.co.apricotdb.ui.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class RemoteRepositoryHandler {

    private static final Logger logger = LoggerFactory.getLogger(RemoteRepositoryHandler.class);

    @Autowired
    ProxyHandler proxyHandler;

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
}
