package za.co.apricotdb.persistence.data;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.repository.ApricotProjectParameterRepository;

/**
 * This manager serves the Parameters, associated with the Project.
 * 
 * @author Anton Nazarov
 * @since 18/02/2019
 */
@Component
public class ProjectParameterManager {

    public static final String DATABASE_CONNECTION_PARAM_PREFIX = "DATABASE.CONNECTION.";
    public static final String DATABASE_CONNECTION_LATEST = "DATABASE.CONNECTION.LATEST";
    public static final String PROJECT_BLACKLIST_PARAM = "PROJECT.BLACKLIST";
    public static final String CONNECTION_SERVER = "CONNECTION.SERVER";
    public static final String CONNECTION_PORT = "CONNECTION.PORT";
    public static final String CONNECTION_DATABASE = "CONNECTION.DATABASE";
    public static final String CONNECTION_SCHEMA = "CONNECTION.SCHEMA";
    public static final String CONNECTION_USER = "CONNECTION.USER";
    public static final String CONNECTION_PASSWORD = "CONNECTION.PASSWORD";
    public static final String PROJECT_DEFAULT_OUTPUT_DIR = "PROJECT.DEFAULT.OUTPUT.DIR";
    public static final String SCRIPT_DEFAULT_OUTPUT_DIR = "SCRIPT.DEFAULT.OUTPUT.DIR";
    public static final String H2DB_FILE_DEFAULT_DIR = "H2DB.FILE.DEFAULT.DIR";

    @Resource
    EntityManager em;
    
    @Resource
    ApricotProjectParameterRepository projectParameterRepository;

    public List<String> getServerNamesForTargetDb(ApricotProject project, String targetDb) {
        List<String> ret = new ArrayList<>();

        List<ApricotProjectParameter> parameters = getConnectionParametersForTargetDb(project, targetDb);
        for (ApricotProjectParameter app : parameters) {
            Properties p = restorePropertiesFromString(app.getValue());
            String pServer = p.getProperty(CONNECTION_SERVER);
            if (pServer != null) {
                if (!ret.contains(pServer)) {
                    ret.add(pServer);
                }
            }
        }

        return ret;
    }

    public List<ApricotProjectParameter> getParametersWithServer(ApricotProject project, String targetDb,
            String server) {
        List<ApricotProjectParameter> ret = new ArrayList<>();

        List<ApricotProjectParameter> parameters = getConnectionParametersForTargetDb(project, targetDb);
        for (ApricotProjectParameter app : parameters) {
            Properties p = restorePropertiesFromString(app.getValue());
            String pServer = p.getProperty(CONNECTION_SERVER);
            if (pServer != null && pServer.equals(server)) {
                ret.add(app);
            }
        }

        return ret;
    }
    
    public ApricotProjectParameter getParameterByName(ApricotProject project, String name) {
        ApricotProjectParameter ret = null;
        TypedQuery<ApricotProjectParameter> query = em
                .createNamedQuery("ApricotProjectParameter.getParameterByName", ApricotProjectParameter.class);
        query.setParameter("project", project);
        query.setParameter("name", name);
        
        List<ApricotProjectParameter> params = query.getResultList();
        if (params != null && params.size() > 0) {
            ret = params.get(0);
        }

        return ret;
    }
    
    public void saveParameter(ApricotProject project, String name, String value) {
        TypedQuery<ApricotProjectParameter> query = em
                .createNamedQuery("ApricotProjectParameter.getParameterByName", ApricotProjectParameter.class);
        query.setParameter("project", project);
        query.setParameter("name", name);
        
        List<ApricotProjectParameter> params = query.getResultList();
        ApricotProjectParameter p = null;
        if (params != null && params.size() > 0) {
            p = params.get(0);
            p.setValue(value);
        } else {
            p = new ApricotProjectParameter();
            p.setProject(project);
            p.setName(name);
            p.setValue(value);
        }
        
        projectParameterRepository.saveAndFlush(p);
    }

    public List<ApricotProjectParameter> getConnectionParametersForTargetDb(ApricotProject project, String targetDb) {
        TypedQuery<ApricotProjectParameter> query = em
                .createNamedQuery("ApricotProjectParameter.getParametersWithPrefix", ApricotProjectParameter.class);
        query.setParameter("project", project);
        query.setParameter("prefix", DATABASE_CONNECTION_PARAM_PREFIX + targetDb);

        return query.getResultList();
    }

    public String getPropertiesAsString(Properties properties, String propName) {
        String ret = null;
        Writer w = new StringWriter();
        try {
            properties.store(w, propName);
            ret = w.toString();
        } catch (IOException ex) {
            ret = "error: " + ex.getMessage();
        }

        return ret;
    }

    public Properties restorePropertiesFromString(String props) {
        Properties properties = new Properties();
        if (!props.startsWith("error:")) {
            Reader r = new StringReader(props);
            try {
                properties.load(r);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return properties;
    }
}
