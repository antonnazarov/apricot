package za.co.apricotdb.metascan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.db2.DB2Scanner;
import za.co.apricotdb.metascan.db2.DB2UrlBuilder;
import za.co.apricotdb.metascan.db2luw.DB2LuwScanner;
import za.co.apricotdb.metascan.db2luw.DB2LuwUrlBuilder;
import za.co.apricotdb.metascan.h2.H2Scanner;
import za.co.apricotdb.metascan.h2.H2UrlBuilder;
import za.co.apricotdb.metascan.mysql.MySqlScanner;
import za.co.apricotdb.metascan.mysql.MySqlUrlBuilder;
import za.co.apricotdb.metascan.oracle.OracleScanner;
import za.co.apricotdb.metascan.oracle.OracleUrlBuilder;
import za.co.apricotdb.metascan.postgresql.PostgreSqlScanner;
import za.co.apricotdb.metascan.postgresql.PostgreSqlUrlBuilder;
import za.co.apricotdb.metascan.sqlite.SqliteScanner;
import za.co.apricotdb.metascan.sqlite.SqliteUrlBuilder;
import za.co.apricotdb.metascan.sqlserver.SqlServerScanner;
import za.co.apricotdb.metascan.sqlserver.SqlServerUrlBuilder;

/**
 * This class recognizes the appropriate scanner to scan the target database.
 *
 * @author Anton Nazarov
 * @since 04/10/2018
 */
@Component
public class MetaDataScannerFactory {

    @Autowired
    SqlServerScanner sqlServerScanner;

    @Autowired
    SqlServerUrlBuilder sqlServerUrlBuilder;

    @Autowired
    H2Scanner h2Scanner;

    @Autowired
    H2UrlBuilder h2UrlBuilder;

    @Autowired
    OracleScanner oracleScanner;

    @Autowired
    OracleUrlBuilder oracleUrlBuilder;

    @Autowired
    MySqlScanner mySqlScanner;

    @Autowired
    MySqlUrlBuilder mySqlUrlBuilder;

    @Autowired
    PostgreSqlScanner postgreSqlScanner;

    @Autowired
    PostgreSqlUrlBuilder postgreSqlUrlBuilder;

    @Autowired
    DB2Scanner db2Scanner;

    @Autowired
    DB2UrlBuilder db2UrlBuilder;

    @Autowired
    DB2LuwScanner db2luwScanner;

    @Autowired
    DB2LuwUrlBuilder db2luwUrlBuilder;

    @Autowired
    SqliteScanner sqliteScanner;

    @Autowired
    SqliteUrlBuilder sqliteUrlBuilder;
    /**
     * Recognize the appropriate scanner.
     */
    public MetaDataScanner getScanner(ApricotTargetDatabase targetDb) {
        MetaDataScanner scanner = null;
        switch (targetDb) {
            case MSSQLServer:
                scanner = sqlServerScanner;
                break;
            case Oracle:
                scanner = oracleScanner;
                break;
            case H2:
                scanner = h2Scanner;
                break;
            case PostrgeSQL:
                scanner = postgreSqlScanner;
                break;
            case MySQL:
                scanner = mySqlScanner;
                break;
            case DB2:
                scanner = db2Scanner;
                break;
            case DB2_LUW:
                scanner = db2luwScanner;
                break;
            case SQLite:
                scanner = sqliteScanner;
                break;
        }

        return scanner;
    }

    /**
     * Recognize the database type, using the given JDBC URL.
     */
    public ApricotTargetDatabase getTargetDatabase(String url) {
        if (url.contains("jdbc:sqlserver://")) {
            return ApricotTargetDatabase.MSSQLServer;
        } else if (url.contains("jdbc:oracle:thin:@")) {
            return ApricotTargetDatabase.Oracle;
        } else if (url.contains("jdbc:h2:")) {
            return ApricotTargetDatabase.H2;
        } else if (url.contains("jdbc:postgresql://")) {
            return ApricotTargetDatabase.PostrgeSQL;
        } else if (url.contains("jdbc:mysql://")) {
            return ApricotTargetDatabase.MySQL;
        } else if (url.contains("jdbc:db2://")) {
            return ApricotTargetDatabase.DB2;
        } else if (url.contains("jdbc:sqlite:")) {
            return ApricotTargetDatabase.SQLite;
        }

        return ApricotTargetDatabase.MSSQLServer;
    }

    public DatabaseUrlBuilder getDatabaseUrlBuilder(ApricotTargetDatabase targetDb) {
        switch (targetDb) {
            case MSSQLServer:
                return sqlServerUrlBuilder;
            case H2:
                return h2UrlBuilder;
            case Oracle:
                return oracleUrlBuilder;
            case PostrgeSQL:
                return postgreSqlUrlBuilder;
            case MySQL:
                return mySqlUrlBuilder;
            case DB2:
                return db2UrlBuilder;
            case DB2_LUW:
                return db2luwUrlBuilder;
            case SQLite:
                return sqliteUrlBuilder;
        }

        return null;
    }

    public String getDefaultSchema(String url, String userName, ApricotTargetDatabase targetDatabase) {
        DatabaseUrlBuilder urlBuilder = getDatabaseUrlBuilder(targetDatabase);
        if (urlBuilder != null) {
            return urlBuilder.getDefaultSchemaName(url, userName);
        }

        return null;
    }

    public String getDriverClass(ApricotTargetDatabase targetDb) {
        DatabaseUrlBuilder urlBuilder = getDatabaseUrlBuilder(targetDb);
        if (urlBuilder != null) {
            return urlBuilder.getDriverClass();
        }

        return null;
    }

    public String getUrl(ApricotTargetDatabase targetDb, String server, String port, String database,
                         boolean integratedSecurity) {
        DatabaseUrlBuilder urlBuilder = getDatabaseUrlBuilder(targetDb);
        if (urlBuilder != null) {
            return urlBuilder.getUrl(server, port, database, integratedSecurity);
        }

        return null;
    }

    public String getTestSQL(ApricotTargetDatabase targetDb) {
        DatabaseUrlBuilder urlBuilder = getDatabaseUrlBuilder(targetDb);
        if (urlBuilder != null) {
            return urlBuilder.getTestSQL();
        }

        return null;
    }
}
