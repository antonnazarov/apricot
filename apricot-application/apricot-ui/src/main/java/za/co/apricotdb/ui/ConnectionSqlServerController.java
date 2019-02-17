package za.co.apricotdb.ui;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import za.co.apricotdb.ui.model.DatabaseConnectionModel;

/**
 * The controller of SQL Server- specific connection.
 * 
 * @author Anton Nazarov
 * @since 17/02/2019
 */
@Component
public class ConnectionSqlServerController {
    
    @FXML
    ComboBox<String> server;
    
    @FXML
    ComboBox<String> port;

    @FXML
    ComboBox<String> database;
    
    @FXML
    ComboBox<String> user;

    @FXML
    PasswordField password;
    
    public void init(DatabaseConnectionModel model) {
        
    }
    
    @FXML
    public void testConnection(ActionEvent event) {
        
    }

    @FXML
    public void cancel(ActionEvent event) {
        
    }
    
    @FXML
    public void forward(ActionEvent event) {
        
    }
}
