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
    public String getUrl(String server, String port, String service) {
        // example of the Oracle URL jdbc:oracle:thin:@localhost:1521:orcl
        StringBuilder sb = new StringBuilder("jdbc:oracle:thin:@").append(server).append(":").append(port).append(":")
                .append(service);

        return sb.toString();
    }

    @Override
    public String getDriverClass() {
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    public String getDefaultSchemaName() {
        return null;
    }
}
