package za.co.apricotdb.ui.handler;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;

@Component
public class SqlServerParametersHandler implements ConnectionParametersHandler {

    @Autowired
    ProjectParameterManager projectParameterManager;

    @Autowired
    ProjectManager projectManager;

    @Override
    public void saveConnectionParameters(Properties params) {
        ApricotProject project = projectManager.findCurrentProject();
        List<ApricotProjectParameter> prm = projectParameterManager.getConnectionParametersForTargetDb(project,
                project.getTargetDatabase());
        String paramsValue = projectParameterManager.getPropertiesAsString(params, "SqlServer connection");
        boolean found = false;
        String idx = null;
        if (prm != null && prm.size() > 0) {
            for (ApricotProjectParameter app : prm) {
                if (idx == null) {
                    idx = getIdx(app.getName());
                }
                Properties p = projectParameterManager.restorePropertiesFromString(app.getValue());

                if (areEqual(p, params, ProjectParameterManager.CONNECTION_PORT)
                        && areEqual(p, params, ProjectParameterManager.CONNECTION_DATABASE)
                        && areEqual(p, params, ProjectParameterManager.CONNECTION_USER)) {
                    // found the same combination of parameters. Rewrite upon the existing one (in
                    // sake of the password).
                    projectParameterManager.saveParameter(project, app.getName(), paramsValue);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            idx = calcNextIdx(idx);
            projectParameterManager.saveParameter(project,
                    ProjectParameterManager.DATABASE_CONNECTION_PARAM_PREFIX + project.getTargetDatabase() + "." + idx,
                    paramsValue);
        }

        // save the latest successful connection for this project
        projectParameterManager.saveParameter(project, ProjectParameterManager.DATABASE_CONNECTION_LATEST, paramsValue);
    }

    public Properties getLatestConnectionProperties(ApricotProject project) {
        Properties ret = null;
        ApricotProjectParameter param = projectParameterManager.getParameterByName(project,
                ProjectParameterManager.DATABASE_CONNECTION_LATEST);

        if (param != null) {
            ret = projectParameterManager.restorePropertiesFromString(param.getValue());
        }

        return ret;
    }

    private String getIdx(String paramName) {
        String idx = null;
        String[] pn = paramName.split("\\.");
        if (pn.length == 4) {
            idx = pn[3];
        }

        return idx;
    }

    private String calcNextIdx(String idx) {
        if (idx == null) {
            return "001";
        }
        return String.format("%03d", Integer.parseInt(idx) + 1);
    }

    private boolean areEqual(Properties p1, Properties p2, String parameterName) {
        return p1.getProperty(parameterName, "<none>").equalsIgnoreCase(p2.getProperty(parameterName, "<none>"));
    }
}
