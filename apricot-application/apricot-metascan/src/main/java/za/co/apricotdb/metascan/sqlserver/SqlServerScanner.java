package za.co.apricotdb.metascan.sqlserver;

import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.StructureScanned;

/**
 * Implementation of the scanner for SQL Server.
 *
 * @author Anton Nazarov
 * @since 26/09/2018
 */
public class SqlServerScanner implements MetaDataScanner {

    @Override
    public StructureScanned scan(String driverClassName, String url, String userName, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
