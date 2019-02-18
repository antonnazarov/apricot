package za.co.apricotdb.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.metascan.ApricotTargetDatabase;

/**
 * The SQL Server connection data model.
 * 
 * @author Anton Nazarov
 * @since 17/02/2019
 */
public class DatabaseConnectionModel  implements Serializable {

    private static final long serialVersionUID = -7558348047467537769L;
    
    private ApricotTargetDatabase targetDb;
    private String server;
    private String port;
    private String database;
    private String user;
    private String password;
    
    private List<String> servers = new ArrayList<>();
    private List<String> databases = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    
    public DatabaseConnectionModel(ApricotTargetDatabase targetDb) {
        this.targetDb = targetDb;
    }
    
    public ApricotTargetDatabase getTargetDb() {
        return targetDb;
    }
    public void setTargetDb(ApricotTargetDatabase targetDb) {
        this.targetDb = targetDb;
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
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getServers() {
        return servers;
    }
    public void setServers(List<String> servers) {
        this.servers = servers;
    }
    public List<String> getDatabases() {
        return databases;
    }
    public void setDatabases(List<String> databases) {
        this.databases = databases;
    }
    public List<String> getUsers() {
        return users;
    }
    public void setUsers(List<String> users) {
        this.users = users;
    }
}
