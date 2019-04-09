package za.co.apricotdb.metascan.h2;

import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.DatabaseUrlBuilder;

/**
 * The builder of H2 specific URL.
 * 
 * @author Anton Nazarov
 * @since 06/04/2019
 */
@Component
public class H2UrlBuilder implements DatabaseUrlBuilder {

    @Override
    public String getUrl(String server, String port, String service) {
        return getUrl(server);
    }

    public String getUrl(String filePath) {
        StringBuilder sb = new StringBuilder("jdbc:h2:file:").append(filePath);

        return sb.toString();
    }

    @Override
    public String getDriverClass() {
        return "org.h2.Driver";
    }

    @Override
    public String getDefaultSchemaName() {
        return "PUBLIC";
    }
}
