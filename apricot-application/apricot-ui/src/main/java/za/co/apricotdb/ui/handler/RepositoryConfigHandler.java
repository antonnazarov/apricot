package za.co.apricotdb.ui.handler;

import com.google.gson.Gson;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ApplicationParameterManager;
import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;
import za.co.apricotdb.ui.RepositoryConfigController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.RepositoryConfiguration;
import za.co.apricotdb.ui.model.RepositoryConfigurationModel;
import za.co.apricotdb.ui.repository.ApricotRepositoryException;
import za.co.apricotdb.ui.repository.RemoteRepositoryService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.GsonFactory;
import za.co.apricotdb.ui.util.ImageHelper;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * The business logic under the Repository Configuration functionality.
 *
 * @author Anton Nazarov
 * @since 05/04/2020
 */
@Component
public class RepositoryConfigHandler {

    public static final String REPOSITORY_CONFIGURATION = "REPOSITORY_CONFIGURATION";

    @Resource
    ApplicationContext context;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    ApplicationParameterManager appParamManager;

    @Autowired
    RemoteRepositoryService remoteRepositoryHandler;

    @ApricotErrorLogger(title = "Unable to create the Repository configuration forms")
    public void showRepositoryConfigForm(boolean showAndWait) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/za/co/apricotdb/ui/apricot-repository-config.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = null;
        try {
            window = loader.load();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Configure Repository");
        dialog.getIcons().add(ImageHelper.getImage("repository-small-s.png", getClass()));

        Scene openProjectScene = new Scene(window);
        dialog.setScene(openProjectScene);
        openProjectScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    dialog.close();
                }
            }
        });

        RepositoryConfigController controller = loader.<RepositoryConfigController>getController();
        controller.init();

        if (showAndWait) {
            dialog.showAndWait();
        } else {
            dialog.show();
        }
    }

    @ApricotErrorLogger(title = "Unable to save the repository configuration")
    public boolean saveRepositoryConfig(RepositoryConfigurationModel model) {
        // check whether the remote URL is not null
        if (StringUtils.isEmpty(model.getRemoteUrl())) {
            Alert alert = alertDec.getErrorAlert("Apricot Repository Configuration",
                    "Please enter the URL of the remote Git- repository");
            alert.showAndWait();
            return false;
        }

        if (model.isUseProxy()
                && (StringUtils.isEmpty(model.getProxyHost()) || StringUtils.isEmpty(model.getProxyPort()))) {
            Alert alert = alertDec.getErrorAlert("Apricot Repository Configuration",
                    "If the \"Use Proxy\" is checked, the Proxy Host and Port have to be provided");
            alert.showAndWait();
            return false;
        }

        RepositoryConfiguration cfg = model.getRepositoryConfiguration();
        Gson gson = GsonFactory.initGson();
        String sConfig = gson.toJson(cfg);
        appParamManager.saveParameter(REPOSITORY_CONFIGURATION, sConfig);

        return true;
    }

    public void checkRemoteRepository(RepositoryConfigurationModel model) {
        Alert alert = null;
        try {
            remoteRepositoryHandler.checkRemoteRepository(model.getRepositoryConfiguration());
            alert = alertDec.getAlert("The Remote Repository Connection Check",
                    "Successfully connected to the Remote Repository", AlertType.INFORMATION);

        } catch (ApricotRepositoryException re) {
            alert = alertDec.getAlert("The Remote Repository Check",
                    "Unable to access the Remote Repository: " + re.getMessage(), AlertType.WARNING);
        }

        alert.showAndWait();
    }

    /**
     * Read the repo configuration from the Application Parameters.
     */
    public RepositoryConfiguration getRepositoryConfiguration() {
        ApricotApplicationParameter param = appParamManager.getParameterByName(REPOSITORY_CONFIGURATION);
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
}
