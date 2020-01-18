package za.co.apricotdb.metascan.db2luw;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.DatabaseUrlBuilder;

/**
 * The URL builder of DB2 LUW.
 * 
 * @author Anton Nazarov
 * @since 13/01/2020
 *
 */
@Component
public class DB2LuwUrlBuilder implements DatabaseUrlBuilder {

    @Override
    public String getDriverClass() {
        return "com.ibm.db2.jcc.DB2Driver";
    }

    @Override
    public String getDefaultSchemaName(String url, String userName) {
        return null;
    }

    @Override
    public String getUrl(String server, String port, String database) {
        // jdbc:db2://host:50001/schema
        StringBuilder sb = new StringBuilder("jdbc:db2://").append(server).append(":").append(port).append("/")
                .append(database);

        return sb.toString();
    }

    @Override
    public String getTestSQL() {
        return "SELECT * FROM SYSIBM.SYSDUMMY1";
    }
}
