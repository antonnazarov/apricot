package za.co.apricotdb.ui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ApplicationParameterManager;
import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;
import za.co.apricotdb.ui.handler.RepositoryConfigHandler;
import za.co.apricotdb.ui.model.RepositoryConfiguration;
import za.co.apricotdb.ui.model.RepositoryConfigurationModel;
import za.co.apricotdb.ui.util.GsonFactory;

/**
 * This controller serves the form apricot-repository-config.fxml.
 * 
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryConfigController {

    public static final String REPOSITORY_CONFIGURATION = "REPOSITORY_CONFIGURATION";

    @Autowired
    ApplicationParameterManager appParamManager;

    @Autowired
    RepositoryConfigHandler repositoryConfigHandler;

    @FXML
    Pane mainPane;

    @FXML
    TextField remoteUrl;

    @FXML
    TextField userName;

    @FXML
    PasswordField password;

    @FXML
    CheckBox useProxy;

    @FXML
    CheckBox httpFlag;

    @FXML
    CheckBox httpsFlag;

    @FXML
    VBox proxyConfig;

    @FXML
    TextField proxyHost;

    @FXML
    TextField proxyPort;

    @FXML
    TextField proxyUser;

    @FXML
    PasswordField proxyPassword;

    private RepositoryConfigurationModel model;

    @FXML
    public void cancel() {
        getStage().close();
    }

    @FXML
    public void save() {
        if (repositoryConfigHandler.saveRepositoryConfig(model)) {
            getStage().close();
        }
    }

    @FXML
    public void checkRepository() {
        repositoryConfigHandler.checkRemoteRepository(model);
    }

    @FXML
    public void setUseProxyFlag() {
        if (useProxy.isSelected()) {
            proxyConfig.setDisable(false);
        } else {
            proxyConfig.setDisable(true);
        }
    }

    /**
     * Initialize the form.
     */
    public void init() {
        // try to read the repo configuration from the Application Parameters
        RepositoryConfiguration repoConfig = getRepositoryConfiguration(
                appParamManager.getParameterByName(REPOSITORY_CONFIGURATION));
        model = new RepositoryConfigurationModel(repoConfig);

        // set the bidirectional binding
        Bindings.bindBidirectional(remoteUrl.textProperty(), model.remoteUrlProperty());
        Bindings.bindBidirectional(userName.textProperty(), model.userNameProperty());
        Bindings.bindBidirectional(password.textProperty(), model.passwordProperty());
        Bindings.bindBidirectional(useProxy.selectedProperty(), model.useProxyProperty());
        Bindings.bindBidirectional(httpFlag.selectedProperty(), model.proxyHttpProperty());
        Bindings.bindBidirectional(httpsFlag.selectedProperty(), model.proxyHttpsProperty());
        Bindings.bindBidirectional(proxyHost.textProperty(), model.proxyHostProperty());
        Bindings.bindBidirectional(proxyPort.textProperty(), model.proxyPortProperty());
        Bindings.bindBidirectional(proxyUser.textProperty(), model.proxyUserProperty());
        Bindings.bindBidirectional(proxyPassword.textProperty(), model.proxyPasswordProperty());

        setUseProxyFlag();
    }

    private RepositoryConfiguration getRepositoryConfiguration(ApricotApplicationParameter param) {
        RepositoryConfiguration repoConfig = new RepositoryConfiguration();

        if (param != null) {
            String sCfg = param.getValue();
            if (StringUtils.isNotEmpty(sCfg)) {
                Gson gson = GsonFactory.initGson();
                repoConfig = gson.fromJson(sCfg, RepositoryConfiguration.class);
            }
        }

        return repoConfig;
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
