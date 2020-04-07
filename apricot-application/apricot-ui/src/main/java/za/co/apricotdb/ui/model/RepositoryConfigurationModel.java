package za.co.apricotdb.ui.model;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import za.co.apricotdb.ui.util.StringEncoder;

/**
 * This is the model class for the Repository Configuration. Supports the
 * bidirectional binding with the user interface.
 * 
 * @author Anton Nazarov
 * @since 06/04/2020
 */
public class RepositoryConfigurationModel {

    private StringProperty remoteUrl = new SimpleStringProperty();
    private StringProperty userName = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private BooleanProperty useProxy = new SimpleBooleanProperty();
    private BooleanProperty proxyHttp = new SimpleBooleanProperty();
    private BooleanProperty proxyHttps = new SimpleBooleanProperty();
    private StringProperty proxyHost = new SimpleStringProperty();
    private StringProperty proxyPort = new SimpleStringProperty();
    private StringProperty proxyUser = new SimpleStringProperty();
    private StringProperty proxyPassword = new SimpleStringProperty();

    public RepositoryConfigurationModel(RepositoryConfiguration cnf) {
        setRemoteUrl(cnf.getRemoteUrl());
        setUserName(cnf.getUserName());
        if (StringUtils.isNotEmpty(cnf.getPassword())) {
            setPassword(StringEncoder.decode(cnf.getPassword()));
        }
        setUseProxy(cnf.isUseProxy());
        setProxyHttp(cnf.isProxyHttp());
        setProxyHttps(cnf.isProxyHttps());
        setProxyHost(cnf.getProxyHost());
        setProxyPort(String.valueOf(cnf.getProxyPort()));
        setProxyUser(cnf.getProxyUser());
        if (StringUtils.isNotEmpty(cnf.getProxyPassword())) {
            setProxyPassword(StringEncoder.decode(cnf.getProxyPassword()));
        }

    }

    // remoteUrl
    public String getRemoteUrl() {
        return remoteUrl.get();
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl.set(remoteUrl);
    }

    public StringProperty remoteUrlProperty() {
        return remoteUrl;
    }

    // userName
    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    // password
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    // useProxy
    public boolean isUseProxy() {
        return useProxy.get();
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy.set(useProxy);
    }

    public BooleanProperty useProxyProperty() {
        return useProxy;
    }

    // proxyHttp
    public boolean isProxyHttp() {
        return proxyHttp.get();
    }

    public void setProxyHttp(boolean proxyHttp) {
        this.proxyHttp.set(proxyHttp);
    }

    public BooleanProperty proxyHttpProperty() {
        return proxyHttp;
    }

    // proxyHttps
    public boolean isProxyHttps() {
        return proxyHttps.get();
    }

    public void setProxyHttps(boolean proxyHttps) {
        this.proxyHttps.set(proxyHttps);
    }

    public BooleanProperty proxyHttpsProperty() {
        return proxyHttps;
    }

    // proxyHost
    public String getProxyHost() {
        return proxyHost.get();
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost.set(proxyHost);
    }

    public StringProperty proxyHostProperty() {
        return proxyHost;
    }

    // proxyPort
    public String getProxyPort() {
        return proxyPort.get();
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort.set(proxyPort);
    }

    public StringProperty proxyPortProperty() {
        return proxyPort;
    }

    // proxyUser
    public String getProxyUser() {
        return proxyUser.get();
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser.set(proxyUser);
    }

    public StringProperty proxyUserProperty() {
        return proxyUser;
    }

    // proxyPassword
    public String getProxyPassword() {
        return proxyPassword.get();
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword.set(proxyPassword);
    }

    public StringProperty proxyPasswordProperty() {
        return proxyPassword;
    }

    public RepositoryConfiguration getRepositoryConfiguration() {
        RepositoryConfiguration ret = new RepositoryConfiguration();
        ret.setRemoteUrl(getRemoteUrl());
        ret.setUserName(getUserName());
        if (StringUtils.isNotEmpty(getPassword())) {
            ret.setPassword(StringEncoder.encode(getPassword()));
        }
        ret.setUseProxy(isUseProxy());
        ret.setProxyHttp(isProxyHttp());
        ret.setProxyHttps(isProxyHttps());
        ret.setProxyHost(getProxyHost());
        if (StringUtils.isNotEmpty(getProxyPort())) {
            ret.setProxyPort(Integer.parseInt(getProxyPort()));
        }
        ret.setProxyUser(getProxyUser());
        if (StringUtils.isNotEmpty(getProxyPassword())) {
            ret.setProxyPassword(StringEncoder.encode(getProxyPassword()));
        }

        return ret;
    }
}
