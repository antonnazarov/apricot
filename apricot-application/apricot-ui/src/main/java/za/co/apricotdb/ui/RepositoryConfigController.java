package za.co.apricotdb.ui;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This controller serves the form apricot-repository-config.fxml.
 * 
 * @author Anton Nazarov
 * @since 04/04/2020
 */
@Component
public class RepositoryConfigController {

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
    
    @FXML
    public void cancel() {
        getStage().close();
    }
    
    @FXML
    public void save() {
        // 
        getStage().close();
    }
    
    @FXML
    public void checkRepository() {
    }
    
    /**
     * Initialize the form.
     */
    public void init() {
        
    }
    
    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
