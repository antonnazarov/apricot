package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.RepositoryConfigHandler;
import za.co.apricotdb.ui.model.RepositoryConfiguration;
import za.co.apricotdb.ui.model.RepositoryConfigurationModel;

/**
 * This controller serves the form apricot-repository-config.fxml.
 * 
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryConfigController {

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
        RepositoryConfiguration repoConfig = repositoryConfigHandler.getRepositoryConfiguration();
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

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
