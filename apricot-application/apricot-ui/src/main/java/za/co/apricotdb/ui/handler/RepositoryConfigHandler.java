package za.co.apricotdb.ui.handler;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.data.ApplicationParameterManager;
import za.co.apricotdb.ui.RepositoryConfigController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.RepositoryConfiguration;
import za.co.apricotdb.ui.model.RepositoryConfigurationModel;
import za.co.apricotdb.ui.repository.ApricotRepositoryException;
import za.co.apricotdb.ui.repository.RemoteRepositoryHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.GsonFactory;

/**
 * The business logic under the Repository Configuration functionality.
 * 
 * @author Anton Nazarov
 * @since 05/04/2020
 */
@Component
public class RepositoryConfigHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    AlertMessageDecorator alertDec;

    @Autowired
    ApplicationParameterManager appParamManager;

    @Autowired
    RemoteRepositoryHandler remoteRepositoryHandler;

    @ApricotErrorLogger(title = "Unable to create the Repository configuration forms")
    public void showRepositoryConfigForm() {
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
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("repository-small-s.png")));

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

        dialog.show();
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
        appParamManager.saveParameter(RepositoryConfigController.REPOSITORY_CONFIGURATION, sConfig);

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
}
