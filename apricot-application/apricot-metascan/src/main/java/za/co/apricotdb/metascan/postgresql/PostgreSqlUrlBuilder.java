package za.co.apricotdb.metascan.postgresql;

import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.DatabaseUrlBuilder;
import za.co.apricotdb.metascan.oracle.OracleServiceType;

/**
 * The PostgreSQL JDBC URL builder.
 * 
 * @author Anton Nazarov
 * @since 07/04/2019
 */
@Component
public class PostgreSqlUrlBuilder implements DatabaseUrlBuilder {

    @Override
    public String getUrl(String server, String port, String database, boolean integratedSecurity, OracleServiceType serviceType, String pathToTnsnamesOraFile) {
        // example of the PostgreSQL URL
        // jdbc:postgresql://<server>:<port5432>/<database>
        StringBuilder sb = new StringBuilder("jdbc:postgresql://").append(server).append(":").append(port).append("/")
                .append(database);

        return sb.toString();
    }

    @Override
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getDefaultSchemaName(String url, String userName) {
        return "public";
    }

    @Override
    public String getTestSQL() {
        return "select table_name from information_schema.tables";
    }
}
