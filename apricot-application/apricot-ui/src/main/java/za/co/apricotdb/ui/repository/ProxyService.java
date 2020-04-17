package za.co.apricotdb.ui.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import za.co.apricotdb.ui.model.RepositoryConfiguration;
import za.co.apricotdb.ui.util.StringEncoder;

/**
 * Helper to configure the Repository Proxy
 * 
 * @author Anton Nazarov
 * @since 07/04/2020
 */
@Component
public class ProxyService {

    public static final String HTTPS_PROXY_HOST_PROP = "https.proxyHost";
    public static final String HTTPS_PROXY_PORT_PROP = "https.proxyPort";
    public static final String HTTPS_PROXY_USER_PROP = "https.proxyUser";
    public static final String HTTPS_PROXY_PASSWORD_PROP = "https.proxyPassword";

    public static final String HTTP_PROXY_HOST_PROP = "http.proxyHost";
    public static final String HTTP_PROXY_PORT_PROP = "http.proxyPort";
    public static final String HTTP_PROXY_USER_PROP = "http.proxyUser";
    public static final String HTTP_PROXY_PASSWORD_PROP = "httpsproxyPassword";

    public void setProxy(RepositoryConfiguration config) {
        resetProxy();
        if (config.isUseProxy()) {
            if (!config.isProxyHttp() && !config.isProxyHttps()) {
             // set default "https" only
                config.setProxyHttps(true);
            }

            if (config.isProxyHttp()) {
                setProxy(config.getProxyHost(), String.valueOf(config.getProxyPort()), config.getProxyUser(),
                        config.getProxyPassword(), false);
            }
            if (config.isProxyHttps()) {
                setProxy(config.getProxyHost(), String.valueOf(config.getProxyPort()), config.getProxyUser(),
                        config.getProxyPassword(), true);
            }
        }
    }

    private void setProxy(String host, String port, String user, String password, boolean https) {
        boolean auth = StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(password);
        if (https) {
            System.setProperty(HTTPS_PROXY_HOST_PROP, host);
            System.setProperty(HTTPS_PROXY_PORT_PROP, port);
            if (auth) {
                System.setProperty(HTTPS_PROXY_USER_PROP, user);
                System.setProperty(HTTPS_PROXY_PASSWORD_PROP, StringEncoder.decode(password));
            }
        } else {
            System.setProperty(HTTP_PROXY_HOST_PROP, host);
            System.setProperty(HTTP_PROXY_PORT_PROP, port);
            if (auth) {
                System.setProperty(HTTP_PROXY_USER_PROP, user);
                System.setProperty(HTTP_PROXY_PASSWORD_PROP, StringEncoder.decode(password));
            }
        }
    }

    public void resetProxy() {
        System.clearProperty(HTTPS_PROXY_HOST_PROP);
        System.clearProperty(HTTPS_PROXY_PORT_PROP);
        System.clearProperty(HTTPS_PROXY_USER_PROP);
        System.clearProperty(HTTPS_PROXY_PASSWORD_PROP);

        System.clearProperty(HTTP_PROXY_HOST_PROP);
        System.clearProperty(HTTP_PROXY_PORT_PROP);
        System.clearProperty(HTTP_PROXY_USER_PROP);
        System.clearProperty(HTTP_PROXY_PASSWORD_PROP);
    }
}
