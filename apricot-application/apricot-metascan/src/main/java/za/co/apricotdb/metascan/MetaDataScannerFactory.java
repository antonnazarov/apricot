package za.co.apricotdb.metascan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.h2.H2Scanner;
import za.co.apricotdb.metascan.mysql.MySqlScanner;
import za.co.apricotdb.metascan.oracle.OracleScanner;
import za.co.apricotdb.metascan.postgresql.PostgreSqlScanner;
import za.co.apricotdb.metascan.sqlserver.SqlServerScanner;

/**
 * This class recognises the appropriate scanner to scan the target database.
 *
 * @author Anton Nazarov
 * @since 04/10/2018
 */
@Component
public class MetaDataScannerFactory {

    @Autowired
    SqlServerScanner sqlServerScanner;

    @Autowired
    H2Scanner h2Scanner;

    @Autowired
    OracleScanner oracleScanner;

    @Autowired
    MySqlScanner mySqlScanner;

    @Autowired
    PostgreSqlScanner postgreSqlScanner;

    /**
     * Recognise the appropriate scanner.
     */
    public MetaDataScanner getScanner(String url) {
        MetaDataScanner scanner = null;
        switch (getTargetDatabase(url)) {
        case MSSQLServer:
            scanner = sqlServerScanner;
            break;
        case Oracle:
            scanner = oracleScanner;
            break;
        case H2:
            scanner = h2Scanner;
            break;
        default:
            scanner = sqlServerScanner;
            break;
        }

        return scanner;
    }

    /**
     * Recognise the database type, using the given JDBC URL.
     */
    private ApricotTargetDatabase getTargetDatabase(String url) {
        if (url.contains("jdbc:sqlserver://")) {
            return ApricotTargetDatabase.MSSQLServer;
        } else if (url.contains("jdbc:oracle:thin:@")) {
            return ApricotTargetDatabase.Oracle;
        } else if (url.contains("jdbc:h2:")) {
            return ApricotTargetDatabase.H2;
        }

        return ApricotTargetDatabase.MSSQLServer;
    }
}
