package za.co.apricotdb.metascan.mysql;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.DatabaseUrlBuilder;

/**
 * The MySQL JDBC URL builder.
 * 
 * @author Anton Nazarov
 * @since 10/04/2019
 */
@Component
public class MySqlUrlBuilder implements DatabaseUrlBuilder {

    @Override
    public String getDriverClass() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getDefaultSchemaName(String url, String userName) {
        if (url == null) {
            return null;
        }
        int idx = url.lastIndexOf("/");
        return url.substring(idx);
    }

    @Override
    public String getUrl(String server, String port, String database, boolean integratedSecurity) {
        // jdbc:mysql://<server>:<port3306>/<database>
        StringBuilder sb = new StringBuilder("jdbc:mysql://").append(server).append(":").append(port).append("/")
                .append(database);
        
        return sb.toString();
    }

    @Override
    public String getTestSQL() {
        return "select table_name from information_schema.tables";
    }
}
