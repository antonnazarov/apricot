package za.co.apricotdb.metascan.oracle;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.DatabaseUrlBuilder;

/**
 * The builder of Oracle specific URL.
 * 
 * @author Anton Nazarov
 * @since 06/04/2019
 */
@Component
public class OracleUrlBuilder implements DatabaseUrlBuilder {

    @Override
    public String getUrl(String server, String port, String service, boolean integratedSecurity, OracleServiceType serviceType, String pathToTnsnamesOraFile) {
        StringBuilder sb = null;
        switch (serviceType) {
            case SERVICE:
                // jdbc:oracle:thin:@//localhost:1521/XE
                sb = new StringBuilder("jdbc:oracle:thin:@//").append(server).append(":").append(port).append("/")
                        .append(service);
                break;
            case TNS:
                // jdbc:oracle:thin:@XE
                sb = new StringBuilder("jdbc:oracle:thin:@").append(service);
                System.setProperty("oracle.net.tns_admin", pathToTnsnamesOraFile);
                break;
            case SID:
                // jdbc:oracle:thin:@localhost:1521:XE
                sb = new StringBuilder("jdbc:oracle:thin:@").append(server).append(":").append(port).append(":")
                        .append(service);
        }

        return sb.toString();
    }

    @Override
    public String getDriverClass() {
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    public String getDefaultSchemaName(String url, String userName) {
        return userName;
    }

    @Override
    public String getTestSQL() {
        return "select table_name from user_tables";
    }
}
