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
        String serverName = params.getProperty(ProjectParameterManager.CONNECTION_SERVER);
        if (serverName != null) {
            ApricotProject project = projectManager.findCurrentProject();
            List<ApricotProjectParameter> prm = projectParameterManager.getParametersWithServer(project,
                    project.getTargetDatabase(), serverName);
            boolean different = true;
            String idx = null;
            if (prm != null && prm.size() > 0) {
                different = false;
                for (ApricotProjectParameter app : prm) {
                    if (idx == null) {
                        idx = getIdx(app.getName());
                    }
                    Properties p = projectParameterManager.restorePropertiesFromString(app.getValue());

                    if (isDifferent(p, params, ProjectParameterManager.CONNECTION_PORT)
                            || isDifferent(p, params, ProjectParameterManager.CONNECTION_DATABASE)
                            || isDifferent(p, params, ProjectParameterManager.CONNECTION_USER)) {
                        different = true;
                        break;
                    }
                }
            }

            if (different) {
                idx = calcNextIdx(idx);
                String paramsValue = projectParameterManager.getPropertiesAsString(params, "SqlServer connection");
                projectParameterManager.saveParameter(project, ProjectParameterManager.DATABASE_CONNECTION_PARAM_PREFIX
                        + project.getTargetDatabase() + "." + idx, paramsValue);
            }
        }
    }

    private String getIdx(String paramName) {
        String idx = null;
        String[] pn = paramName.split(".");
        if (pn.length == 3) {
            idx = pn[2];
        }

        return idx;
    }

    private String calcNextIdx(String idx) {
        if (idx == null) {
            return "001";
        }
        return String.format("%03d", Integer.parseInt(idx) + 1);
    }

    private boolean isDifferent(Properties p1, Properties p2, String parameterName) {
        return !p1.getProperty(parameterName, "<none>").equalsIgnoreCase(p2.getProperty(parameterName, "<none>"));
    }
}
