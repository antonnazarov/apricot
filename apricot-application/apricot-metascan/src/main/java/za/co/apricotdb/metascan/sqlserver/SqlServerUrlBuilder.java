package za.co.apricotdb.metascan.sqlserver;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.DatabaseUrlBuilder;

/**
 * A builder of the SQL Server- specific URL from teh given parameters.
 * 
 * @author Anton Nazarov
 * @since 18/02/2019
 */
@Component
public class SqlServerUrlBuilder implements DatabaseUrlBuilder {

    public String getUrl(String server, String port, String database) {
        // example of the SQLServer UR:L
        // jdbc:sqlserver://DST15404:1433;databaseName=apricot-tests
        StringBuilder sb = new StringBuilder("jdbc:sqlserver://").append(server).append(":").append(port).append(";")
                .append("databaseName=").append(database);

        return sb.toString();
    }

    public String getDriverClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }
}
