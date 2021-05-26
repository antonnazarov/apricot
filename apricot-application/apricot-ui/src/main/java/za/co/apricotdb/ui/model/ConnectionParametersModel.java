package za.co.apricotdb.ui.model;

import org.apache.commons.lang3.StringUtils;
import za.co.apricotdb.ui.util.StringEncoder;

import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_DATABASE;
import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_PASSWORD;
import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_PORT;
import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_SCHEMA;
import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_SERVER;
import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_SERVICE_TYPE;
import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_TNSNAMESORA_PATH;
import static za.co.apricotdb.persistence.data.ProjectParameterManager.CONNECTION_USER;

/**
 * This bean represents the parameters of successful connections stored as
 * an application- level parameter.
 *
 * @author Anton Nazarov
 * @since 08/07/2020
 */
public class ConnectionParametersModel implements Serializable {

    private String id;
    private String dbType;
    private String server;
    private String port;
    private String database;
    private String schema;
    private String user;
    private String password;
    private String serviceType;      //  used for Oracle, might be Service/SID/TNS
    private String tnsNamesOraPath;  //  used for Oracle

    /**
     * Construct the connection parameters model.
     */
    public ConnectionParametersModel(String dbType, String server, String port, String database, String schema,
                                     String user, String password, String serviceType, String tnsNamesOraPath) {
        this.dbType = dbType;
        this.server = server;
        this.port = port;
        this.database = database;
        this.schema = schema;
        this.user = user;
        setPassword(password);
        this.serviceType = serviceType;
        this.tnsNamesOraPath = tnsNamesOraPath;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return StringEncoder.decode(password);
    }

    public void setPassword(String password) {
        this.password = StringEncoder.encode(password);
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTnsNamesOraPath() {
        return tnsNamesOraPath;
    }

    public void setTnsNamesOraPath(String tnsNamesOraPath) {
        this.tnsNamesOraPath = tnsNamesOraPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionParametersModel that = (ConnectionParametersModel) o;
        return Objects.equals(dbType, that.dbType) && Objects.equals(server, that.server) && Objects.equals(port,
                that.port) && Objects.equals(database, that.database) && Objects.equals(schema, that.schema) && Objects.equals(user, that.user) && Objects.equals(serviceType, that.serviceType) && Objects.equals(tnsNamesOraPath, that.tnsNamesOraPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbType, server, port, database, schema, user, serviceType, tnsNamesOraPath);
    }

    public Properties getAsProperties() {
        Properties ret = new Properties();
        if (StringUtils.isNotEmpty(server)) {
            ret.put(CONNECTION_SERVER, server);
        }
        if (StringUtils.isNotEmpty(port)) {
            ret.put(CONNECTION_PORT, port);
        }
        if (StringUtils.isNotEmpty(database)) {
            ret.put(CONNECTION_DATABASE, database);
        }
        if (StringUtils.isNotEmpty(schema)) {
            ret.put(CONNECTION_SCHEMA, schema);
        }
        if (StringUtils.isNotEmpty(user)) {
            ret.put(CONNECTION_USER, user);
        }
        if (StringUtils.isNotEmpty(password)) {
            ret.put(CONNECTION_PASSWORD, getPassword());
        }
        if (StringUtils.isNotEmpty(serviceType)) {
            ret.put(CONNECTION_SERVICE_TYPE, serviceType);
        }
        if (StringUtils.isNotEmpty(tnsNamesOraPath)) {
            ret.put(CONNECTION_TNSNAMESORA_PATH, tnsNamesOraPath);
        }

        return ret;
    }
}
