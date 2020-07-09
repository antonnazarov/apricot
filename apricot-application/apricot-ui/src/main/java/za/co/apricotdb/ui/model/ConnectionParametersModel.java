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

    /**
     * Construct the connection parameters model.
     */
    public ConnectionParametersModel(String dbType, String server, String port, String database, String schema,
                                     String user, String password) {
        this.dbType = dbType;
        this.server = server;
        this.port = port;
        this.database = database;
        this.schema = schema;
        this.user = user;
        setPassword(password);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectionParametersModel that = (ConnectionParametersModel) o;

        if (!dbType.equals(that.dbType)) return false;
        if (!server.equals(that.server)) return false;
        if (!port.equals(that.port)) return false;
        if (!database.equals(that.database)) return false;
        if (!Objects.equals(schema, that.schema)) return false;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        int result = dbType.hashCode();
        result = 31 * result + server.hashCode();
        result = 31 * result + port.hashCode();
        result = 31 * result + database.hashCode();
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public Properties getAsProperties() {
        Properties ret = new Properties();
        ret.put(CONNECTION_SERVER, server);
        ret.put(CONNECTION_PORT, port);
        ret.put(CONNECTION_DATABASE, database);
        if (StringUtils.isNotEmpty(schema)) {
            ret.put(CONNECTION_SCHEMA, schema);
        }
        if (StringUtils.isNotEmpty(user)) {
            ret.put(CONNECTION_USER, user);
        }
        if (StringUtils.isNotEmpty(password)) {
            ret.put(CONNECTION_PASSWORD, getPassword());
        }

        return ret;
    }
}
