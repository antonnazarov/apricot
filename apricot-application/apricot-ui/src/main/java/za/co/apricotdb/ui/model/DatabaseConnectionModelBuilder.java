package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.ui.util.StringEncoder;

@Component
public class DatabaseConnectionModelBuilder {

    @Autowired
    ProjectParameterManager projectParameterManager;

    @Autowired
    ProjectManager projectManager;

    public DatabaseConnectionModel buildModel(ApricotProject project) {
        DatabaseConnectionModel model = new DatabaseConnectionModel(
                ApricotTargetDatabase.valueOf(project.getTargetDatabase()));

        List<String> servers = new ArrayList<>();
        for (ApricotProject p : projectManager.getAllProjects()) {
            List<String> srvrs = projectParameterManager.getServerNamesForTargetDb(p, project.getTargetDatabase());
            if (srvrs != null && srvrs.size() > 0) {
                for (String srv : srvrs) {
                    if (!servers.contains(srv)) {
                        servers.add(srv);
                    }
                }

            }
        }
        Collections.sort(servers);
        model.setServers(servers);

        return model;
    }

    /**
     * Populate the model, based on the current server selected.
     */
    public void populateModel(ApricotProject project, DatabaseConnectionModel model, String server) {
        List<ApricotProjectParameter> prms = new ArrayList<>();
        for (ApricotProject p : projectManager.getAllProjects()) {
            prms.addAll(projectParameterManager.getParametersWithServer(p, project.getTargetDatabase(), server));
        }

        model.getDatabases().clear();
        model.getUsers().clear();

        for (ApricotProjectParameter p : prms) {
            Properties props = projectParameterManager.restorePropertiesFromString(p.getValue());
            String sDatabase = props.getProperty(ProjectParameterManager.CONNECTION_DATABASE);
            String sPort = props.getProperty(ProjectParameterManager.CONNECTION_PORT);
            String sUser = props.getProperty(ProjectParameterManager.CONNECTION_USER);
            if (!model.getDatabases().contains(sDatabase)) {
                model.getDatabases().add(sDatabase);
            }
            model.setPort(sPort);
            if (!model.getUsers().contains(sUser)) {
                model.getUsers().add(sUser);
            }

            model.savePassword(sUser,
                    StringEncoder.decode(props.getProperty(ProjectParameterManager.CONNECTION_PASSWORD)));
        }
    }
}