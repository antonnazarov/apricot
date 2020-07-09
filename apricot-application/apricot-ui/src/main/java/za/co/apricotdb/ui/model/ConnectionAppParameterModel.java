package za.co.apricotdb.ui.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * The Json ready model for the application level parameter of the history of the successful connections.
 *
 * @author Anton Nazarov
 * @since 08/07/2020
 */
public class ConnectionAppParameterModel implements Serializable {

    private List<ConnectionParametersModel> connectionParameters = new ArrayList<>();
    private String latestConnectionId;

    public List<ConnectionParametersModel> getConnectionParameters() {
        return connectionParameters;
    }

    public void setConnectionParameters(List<ConnectionParametersModel> connectionParameters) {
        this.connectionParameters = connectionParameters;
    }

    public String getLatestConnectionId() {
        return latestConnectionId;
    }

    public void setLatestConnectionId(String latestConnectionId) {
        this.latestConnectionId = latestConnectionId;
    }

    public boolean addConnection(ConnectionParametersModel connectionModel) {
        boolean found = false;
        for (ConnectionParametersModel m : connectionParameters) {
            if (m.equals(connectionModel)) {
                latestConnectionId = m.getId();
                found = true;
                break;
            }
        }

        if (!found) {
            String id = UUID.randomUUID().toString();
            connectionModel.setId(id);
            connectionParameters.add(connectionModel);
            latestConnectionId = id;
        }

        return true;
    }

    /**
     * Get the most "fresh" successful connection parameters.
     */
    public ConnectionParametersModel getLatestSuccessfulConnection() {
        if (StringUtils.isNotEmpty(latestConnectionId) && !connectionParameters.isEmpty()) {
            for (ConnectionParametersModel m : connectionParameters) {
                if (latestConnectionId.equals(m.getId())) {
                    return m;
                }
            }
        }

        if (!connectionParameters.isEmpty()) {
            return connectionParameters.get(0);
        }

        return null;
    }

    /**
     * Get the unique values for all major connection parameters as lists.
     */
    public Map<String, List<String>> getValuesMap() {
        if (connectionParameters.isEmpty()) {
            return null;
        }

        Set<String> servers = new HashSet<>();
        Set<String> ports = new HashSet<>();
        Set<String> databases = new HashSet<>();
        Set<String> schemas = new HashSet<>();
        Set<String> users = new HashSet<>();
        for (ConnectionParametersModel m : connectionParameters) {
            servers.add(m.getServer());
            ports.add(m.getPort());
            databases.add(m.getDatabase());
            if (StringUtils.isNotEmpty(m.getSchema())) {
                schemas.add(m.getSchema());
            }
            if (StringUtils.isNotEmpty(m.getUser())) {
                users.add(m.getUser());
            }
        }

        Map<String, List<String>> ret = new HashMap<>();
        ret.put("servers", new ArrayList<>(servers));
        ret.put("ports", new ArrayList<>(ports));
        ret.put("databases", new ArrayList<>(databases));
        ret.put("schemas", new ArrayList<>(schemas));
        ret.put("users", new ArrayList<>(users));

        return ret;
    }
}
