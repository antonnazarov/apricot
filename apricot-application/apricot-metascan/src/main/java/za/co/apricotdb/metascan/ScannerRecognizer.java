package za.co.apricotdb.metascan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.h2.H2Scanner;
import za.co.apricotdb.metascan.mysql.MySqlScanner;
import za.co.apricotdb.metascan.oracle.OracleScanner;
import za.co.apricotdb.metascan.sqlserver.SqlServerScanner;

/**
 * This class recognizes the appropriate scanner to scan the target database.
 *
 * @author Anton Nazarov
 * @since 04/10/2018
 */
@Component
public class ScannerRecognizer {

    @Autowired
    SqlServerScanner sqlServerScanner;
    
    @Autowired
    H2Scanner h2Scanner;
    
    @Autowired
    OracleScanner oracleScanner;
    
    @Autowired
    MySqlScanner mySqlScanner;

    /**
     * Recognize the appropriate scanner.
     */
    public MetaDataScanner getScanner(String url) {
        if (isSqlServer(url)) {
            return sqlServerScanner;
        }
        
        return null;
    }
    
    private boolean isSqlServer(String url) {
        return url.contains("jdbc:sqlserver://");
    }
}
