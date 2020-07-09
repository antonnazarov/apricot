package za.co.apricotdb.ui.handler;

import java.util.Properties;

/**
 * This interface describes operations common for connection handlers, implemented for different databases.
 * 
 * @author Anton Nazarov
 * @since 20/02/2019
 */
public interface ConnectionParametersHandler {
    
    void saveConnectionParameters(String dbType, String server, String port, String database, String schema,
                                  String user, String password);
}
