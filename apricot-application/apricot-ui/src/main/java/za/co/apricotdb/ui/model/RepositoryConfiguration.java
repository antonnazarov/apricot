package za.co.apricotdb.ui.model;

import java.io.Serializable;

/**
 * This bean helps to serialize/de-serialize the configuration of Apricot
 * Repository using json.
 * 
 * @author Anton Nazarov
 * @since 06/04/2020
 */
public class RepositoryConfiguration implements Serializable {

    private static final long serialVersionUID = 3112648564810728981L;

    private String remoteUrl;
    private String userName;
    private String password;
    private boolean useProxy;
    private boolean proxyHttp;
    private boolean proxyHttps;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPassword;

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public boolean isProxyHttp() {
        return proxyHttp;
    }

    public void setProxyHttp(boolean proxyHttp) {
        this.proxyHttp = proxyHttp;
    }

    public boolean isProxyHttps() {
        return proxyHttps;
    }

    public void setProxyHttps(boolean proxyHttps) {
        this.proxyHttps = proxyHttps;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    @Override
    public String toString() {
        return "RepositoryConfiguration [remoteUrl=" + remoteUrl + ", userName=" + userName + ", password=" + password
                + ", useProxy=" + useProxy + ", proxyHttp=" + proxyHttp + ", proxyHttps=" + proxyHttps + ", proxyHost="
                + proxyHost + ", proxyPort=" + proxyPort + ", proxyUser=" + proxyUser + ", proxyPassword="
                + proxyPassword + "]";
    }
}
