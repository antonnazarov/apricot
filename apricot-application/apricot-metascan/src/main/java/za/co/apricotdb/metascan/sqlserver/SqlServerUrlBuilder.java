package za.co.apricotdb.metascan.sqlserver;

import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.DatabaseUrlBuilder;
import za.co.apricotdb.metascan.oracle.OracleServiceType;

/**
 * A builder of the SQL Server- specific URL from the given parameters.
 * 
 * @author Anton Nazarov
 * @since 18/02/2019
 */
@Component
public class SqlServerUrlBuilder implements DatabaseUrlBuilder {

    @Override
    public String getUrl(String server, String port, String database, boolean integratedSecurity, OracleServiceType serviceType, String pathToTnsnamesOraFile) {
        // example of the SQLServer URL
        // jdbc:sqlserver://DST15404:1433;databaseName=apricot-tests
        StringBuilder sb = new StringBuilder("jdbc:sqlserver://").append(server).append(":").append(port).append(";")
                .append("databaseName=").append(database);
        if (integratedSecurity) {
            sb.append(";integratedSecurity=true");
        }

        return sb.toString();
    }

    public String getDriverClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getDefaultSchemaName(String url, String userName) {
        return "dbo";
    }

    @Override
    public String getTestSQL() {
        return "select name from sys.tables;";
    }
}
