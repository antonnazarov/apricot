package za.co.apricotdb.metascan.sqlite;

import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.DatabaseUrlBuilder;

/**
 * The builder of SQLite specific URL.
 * 
 * @author Anton Nazarov
 * @since 18/09/2020
 */
@Component
public class SqliteUrlBuilder implements DatabaseUrlBuilder {

    @Override
    public String getUrl(String server, String port, String service, boolean integratedSecurity) {
        return getUrl(server);
    }

    public String getUrl(String filePath) {
        StringBuilder sb = new StringBuilder("jdbc:sqlite:").append(filePath);

        return sb.toString();
    }

    @Override
    public String getDriverClass() {
        return "org.sqlite.JDBC";
    }

    @Override
    public String getDefaultSchemaName(String url, String userName) {
        return null;
    }

    @Override
    public String getTestSQL() {
        return "select tbl_name, sql from sqlite_master";
    }
}
