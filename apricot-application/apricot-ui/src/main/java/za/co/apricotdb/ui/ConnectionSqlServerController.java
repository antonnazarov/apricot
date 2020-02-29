package za.co.apricotdb.ui;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.MetaDataScannerFactory;
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
import za.co.apricotdb.ui.model.DatabaseConnectionModelBuilder;
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
    AlertMessageDecorator alertDecorator;

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    SqlServerParametersHandler parametersHandler;

    @Autowired
    DatabaseConnectionModelBuilder dbConnModelBuilder;

    @Autowired
    MetaDataScannerFactory scannerFactory;

    @FXML
    Pane mainPane;

    @FXML
    ComboBox<String> server;

    @FXML
    ComboBox<String> port;

    @FXML
    ComboBox<String> database;

    @FXML
    ComboBox<String> schema;

    @FXML
    ComboBox<String> user;

    @FXML
    PasswordField password;

    @FXML
    Label serviceLabel;

    @FXML
    CheckBox useWindowsUserFlag;

    private DatabaseConnectionModel model;
    private ApricotSnapshot snapshot;
    private ApricotProject project;

    public void init(DatabaseConnectionModel model) {
        this.model = model;

        this.snapshot = snapshotManager.getDefaultSnapshot();
        this.project = projectManager.findCurrentProject();

        applyModel(model);
        setLatestConnectionParameters();

        if (model.getTargetDb() == ApricotTargetDatabase.Oracle) {
            serviceLabel.setText("SID:");
            schema.setDisable(true);
            // the schema and user name are the same for the Oracle database
            schema.valueProperty().bind(user.valueProperty());
        }

        if (model.getTargetDb() == ApricotTargetDatabase.MSSQLServer) {
            useWindowsUserFlag.setVisible(true);
        }
    }

    @FXML
    public void testConnection(ActionEvent event) {
        reverseEngineHandler.testConnection(server.getSelectionModel().getSelectedItem(),
                port.getSelectionModel().getSelectedItem(), database.getSelectionModel().getSelectedItem(),
                schema.getSelectionModel().getSelectedItem(), user.getSelectionModel().getSelectedItem(),
                password.getText(), model.getTargetDb(), useWindowsUserFlag.isSelected());
        Alert alert = getAlert(AlertType.INFORMATION, "The connection was successfully established");
        alert.showAndWait();
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void forward(ActionEvent event) {
        // check the connection firstly
        reverseEngineHandler.testConnection(server.getSelectionModel().getSelectedItem(),
                port.getSelectionModel().getSelectedItem(), database.getSelectionModel().getSelectedItem(),
                schema.getSelectionModel().getSelectedItem(), user.getSelectionModel().getSelectedItem(),
                password.getText(), model.getTargetDb(), useWindowsUserFlag.isSelected());

        String driverClass = scannerFactory.getDriverClass(model.getTargetDb());
        String url = scannerFactory.getUrl(model.getTargetDb(), server.getSelectionModel().getSelectedItem(),
                port.getSelectionModel().getSelectedItem(), database.getSelectionModel().getSelectedItem(),
                useWindowsUserFlag.isSelected());

        MetaData metaData = reverseEngineHandler.getMetaData(model.getTargetDb(), driverClass, url, schema.getValue(),
                user.getSelectionModel().getSelectedItem(), password.getText(), snapshot);
        String[] blackList = blackListHandler.getBlackListTables(project);

        getStage().close();
        reverseEngineHandler.openScanResultForm(metaData, blackList, composeReverseEngineeringParameters());
    }

    @FXML
    public void serverSelected(ActionEvent event) {
        dbConnModelBuilder.populateModel(project, model, server.getSelectionModel().getSelectedItem());
        applyModel(model);
    }

    @FXML
    public void userSelected(ActionEvent event) {
        password.setText(model.getPassword(user.getSelectionModel().getSelectedItem()));
    }

    @FXML
    public void useWindowsUser(ActionEvent event) {
        if (useWindowsUserFlag.isSelected()) {
            // switch to the user authentication
            user.setDisable(true);
            password.setDisable(true);
        } else {
            user.setDisable(false);
            password.setDisable(false);
        }
    }

    private void applyModel(DatabaseConnectionModel model) {
        port.getItems().clear();
        database.getItems().clear();
        user.getItems().clear();
        schema.getItems().clear();

        if (server.getItems().isEmpty()) {
            server.getItems().addAll(model.getServers());
        }

        if (model.getPort() != null) {
            port.getItems().add(model.getPort());
            port.getSelectionModel().select(model.getPort());
        }

        database.getItems().addAll(model.getDatabases());
        if (model.getDatabase() != null) {
            database.getSelectionModel().select(model.getDatabase());
        }

        schema.getItems().addAll(model.getSchemas());
        if (model.getSchema() != null) {
            schema.getSelectionModel().select(model.getSchema());
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

    private void setLatestConnectionParameters() {
        Properties props = parametersHandler.getLatestConnectionProperties(project);
        if (props != null) {
            String sServer = props.getProperty(ProjectParameterManager.CONNECTION_SERVER);
            if (sServer != null && server.getItems() != null && server.getItems().contains(sServer)) {
                server.getSelectionModel().select(sServer);
                serverSelected(new ActionEvent());
            }
            if (props.getProperty(ProjectParameterManager.CONNECTION_PORT) != null) {
                port.setValue(props.getProperty(ProjectParameterManager.CONNECTION_PORT));
            }
            if (props.getProperty(ProjectParameterManager.CONNECTION_DATABASE) != null) {
                database.setValue(props.getProperty(ProjectParameterManager.CONNECTION_DATABASE));
            }
            if (props.getProperty(ProjectParameterManager.CONNECTION_SCHEMA) != null) {
                schema.setValue(props.getProperty(ProjectParameterManager.CONNECTION_SCHEMA));
            }
            if (props.getProperty(ProjectParameterManager.CONNECTION_USER) != null) {
                user.setValue(props.getProperty(ProjectParameterManager.CONNECTION_USER));
            }
            if (props.getProperty(ProjectParameterManager.CONNECTION_PASSWORD) != null) {
                password.setText(StringEncoder.decode(props.getProperty(ProjectParameterManager.CONNECTION_PASSWORD)));
            }
        }
    }

    private String composeReverseEngineeringParameters() {
        StringBuilder sb = new StringBuilder();

        sb.append("Database Type: ").append(model.getTargetDb().name()).append("\n");
        sb.append("Server: ").append(server.getSelectionModel().getSelectedItem()).append("\n");
        sb.append("Port: ").append(port.getSelectionModel().getSelectedItem()).append("\n");
        sb.append("Database: ").append(database.getSelectionModel().getSelectedItem()).append("\n");
        sb.append("User: ").append(user.getSelectionModel().getSelectedItem()).append("\n");

        return sb.toString();
    }
}
