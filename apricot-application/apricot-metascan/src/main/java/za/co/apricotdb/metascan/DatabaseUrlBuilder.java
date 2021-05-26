package za.co.apricotdb.metascan;

import za.co.apricotdb.metascan.oracle.OracleServiceType;

/**
 * All JDBC URL builders have to implement this interface.
 * 
 * @author Anton Nazarov
 * @since 06/04/2019
 */
public interface DatabaseUrlBuilder {
    
    String getDriverClass();
    
    String getDefaultSchemaName(String url, String userName);
    
    String getUrl(String server, String port, String database, boolean integratedSecurity,
                  OracleServiceType serviceType, String pathToTnsnamesOraFile);
    
    String getTestSQL();
}
