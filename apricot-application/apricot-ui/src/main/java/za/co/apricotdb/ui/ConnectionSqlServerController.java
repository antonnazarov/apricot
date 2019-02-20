package za.co.apricotdb.ui;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.sqlserver.SqlServerScanner;
import za.co.apricotdb.metascan.sqlserver.SqlServerUrlBuilder;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.handler.BlackListHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.ui.handler.SqlServerParametersHandler;
import za.co.apricotdb.ui.model.DatabaseConnectionModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.StringEncoder;

/**
 * The controller of SQL Server- specific connection.
 * 
 * @author Anton Nazarov
 * @since 17/02/2019
 */
@Component
public class ConnectionSqlServerController {

    @Autowired
    SqlServerUrlBuilder sqlServerUrlBuilder;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    SqlServerScanner sqlServerScanner;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    BlackListHandler blackListHandler;
    
    @Autowired
    SqlServerParametersHandler parametersHandler;

    @FXML
    Pane mainPane;

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

    private PropertyChangeListener canvasChangeListener;

    public void init(DatabaseConnectionModel model, PropertyChangeListener canvasChangeListener) {
        this.canvasChangeListener = canvasChangeListener;
        applyModel(model);
    }

    @FXML
    public boolean testConnection(ActionEvent event) {
        Alert alert = null;
        try {
            testConnection(server.getSelectionModel().getSelectedItem(), port.getSelectionModel().getSelectedItem(),
                    database.getSelectionModel().getSelectedItem(), user.getSelectionModel().getSelectedItem(),
                    password.getText());
            alert = getAlert(AlertType.INFORMATION, "The connection was successfully established");
            alert.showAndWait();
        } catch (Exception e) {
            alert = getAlert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void testConnection(String server, String port, String database, String user, String password)
            throws Exception {
        String driverClass = sqlServerUrlBuilder.getDriverClass();
        String url = sqlServerUrlBuilder.getUrl(server, port, database);

        try {
            JdbcOperations op = MetaDataScanner.getTargetJdbcOperations(driverClass, url, user, password);

            RowMapper<String> rowMapper = (rs, rowNum) -> {
                return rs.getString("name");
            };
            op.query("select name from sys.tables;", rowMapper);
            
            //  Success! Save the connection parameters in the project- parameter
            Properties params = getConnectionParameters();
            parametersHandler.saveConnectionParameters(params);
        } catch (Exception e) {
            throw new Exception("Unable to connect to the database server:\n" + WordUtils.wrap(e.getMessage(), 60));
        }
    }
    
    private Properties getConnectionParameters() {
        Properties params = new Properties();
        
        params.setProperty(ProjectParameterManager.CONNECTION_SERVER, server.getSelectionModel().getSelectedItem());
        params.setProperty(ProjectParameterManager.CONNECTION_PORT, port.getSelectionModel().getSelectedItem());
        params.setProperty(ProjectParameterManager.CONNECTION_DATABASE, database.getSelectionModel().getSelectedItem());
        params.setProperty(ProjectParameterManager.CONNECTION_USER, user.getSelectionModel().getSelectedItem());
        params.setProperty(ProjectParameterManager.CONNECTION_PASSWORD, StringEncoder.encode(password.getText()));
        
        return params;
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void forward(ActionEvent event) {
        // check the connection firstly
        try {
            testConnection(server.getSelectionModel().getSelectedItem(), port.getSelectionModel().getSelectedItem(),
                    database.getSelectionModel().getSelectedItem(), user.getSelectionModel().getSelectedItem(),
                    password.getText());
        } catch (Exception e) {
            Alert alert = getAlert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            return;
        }

        String driverClass = sqlServerUrlBuilder.getDriverClass();
        String url = sqlServerUrlBuilder.getUrl(server.getSelectionModel().getSelectedItem(),
                port.getSelectionModel().getSelectedItem(), database.getSelectionModel().getSelectedItem());
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        ApricotProject project = projectManager.findCurrentProject();

        MetaData metaData = sqlServerScanner.scan(driverClass, url, user.getSelectionModel().getSelectedItem(),
                password.getText(), snapshot);
        String[] blackList = blackListHandler.getBlackListTables(project);
        try {
            getStage().close();
            reverseEngineHandler.openScanResultForm(metaData, blackList, canvasChangeListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void serverSelected(ActionEvent event) {

    }

    private void applyModel(DatabaseConnectionModel model) {
        server.getItems().clear();
        port.getItems().clear();
        database.getItems().clear();
        user.getItems().clear();

        // TODO cant't do it here??
        server.getItems().addAll(model.getServers());
        if (model.getServer() != null) {
            server.getSelectionModel().select(model.getServer());
        }

        if (model.getPort() != null) {
            port.getItems().add(model.getPort());
            port.getSelectionModel().select(model.getPort());
        }

        database.getItems().addAll(model.getDatabases());
        if (model.getDatabase() != null) {
            database.getSelectionModel().select(model.getDatabase());
        }

        user.getItems().addAll(model.getUsers());
        if (model.getUser() != null) {
            user.getSelectionModel().select(model.getUser());
        }

        password.setText(model.getPassword());
    }

    private Alert getAlert(AlertType alertType, String text) {
        Alert alert = new Alert(alertType, null, ButtonType.OK);
        alert.setTitle("Test Connection");
        alert.setHeaderText(text);
        if (alertType == AlertType.ERROR) {
            alertDecorator.decorateAlert(alert);
        }

        return alert;
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
