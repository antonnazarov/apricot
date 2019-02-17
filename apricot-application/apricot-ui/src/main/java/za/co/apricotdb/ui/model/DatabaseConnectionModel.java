package za.co.apricotdb.ui.model;

import java.io.Serializable;

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
    

}
