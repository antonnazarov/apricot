package za.co.apricotdb.ui;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.MetaDataScannerFactory;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.handler.BlackListHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.ui.model.ConnectionAppParameterModel;
import za.co.apricotdb.ui.model.ConnectionParametersModel;
import za.co.apricotdb.ui.service.ReverseEngineService;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.util.List;
import java.util.Map;

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
    MetaDataScannerFactory scannerFactory;

    @Autowired
    ReverseEngineService reverseEngineService;

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

    private ConnectionAppParameterModel model;
    private ApricotSnapshot snapshot;
    private ApricotProject project;
    private ApricotTargetDatabase targetDb;

    public void init(ConnectionAppParameterModel model) {
        this.model = model;
        snapshot = snapshotManager.getDefaultSnapshot();
        project = projectManager.findCurrentProject();
        targetDb = ApricotTargetDatabase.parse(project.getTargetDatabase());
        applyModel(model);

        if (targetDb == ApricotTargetDatabase.Oracle) {
            serviceLabel.setText("SID:");
            schema.setDisable(true);
            // the schema and user name are the same for the Oracle database
            schema.valueProperty().bind(user.valueProperty());
        }

        if (targetDb == ApricotTargetDatabase.MSSQLServer) {
            useWindowsUserFlag.setVisible(true);
        }
    }

    @FXML
    public void testConnection(ActionEvent event) {
        reverseEngineHandler.testConnection(server.getSelectionModel().getSelectedItem(),
                port.getSelectionModel().getSelectedItem(), database.getSelectionModel().getSelectedItem(),
                schema.getSelectionModel().getSelectedItem(), user.getSelectionModel().getSelectedItem(),
                password.getText(), targetDb, useWindowsUserFlag.isSelected());
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
                password.getText(), targetDb, useWindowsUserFlag.isSelected());

        String driverClass = scannerFactory.getDriverClass(targetDb);
        String url = scannerFactory.getUrl(targetDb, server.getSelectionModel().getSelectedItem(),
                port.getSelectionModel().getSelectedItem(), database.getSelectionModel().getSelectedItem(),
                useWindowsUserFlag.isSelected());

        reverseEngineService.initService(targetDb, driverClass, url, schema.getValue(),
                user.getSelectionModel().getSelectedItem(), password.getText(), snapshot);
        reverseEngineService.setOnSucceeded(e -> {
            getStage().close();
            reverseEngineHandler.openScanResultForm(reverseEngineService.getValue(), blackListHandler.getBlackListTables(project),
                    composeReverseEngineeringParameters());
        });
        reverseEngineService.setOnFailed(e -> {
            throw new IllegalArgumentException(reverseEngineService.getException());
        });
        reverseEngineService.start();
    }

    @FXML
    public void serverSelected(ActionEvent event) {
        //  no action on the server selected
    }

    @FXML
    public void userSelected(ActionEvent event) {
        //  no action on the user selected
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

    private void applyModel(ConnectionAppParameterModel model) {
        server.getItems().clear();
        port.getItems().clear();
        database.getItems().clear();
        schema.getItems().clear();
        user.getItems().clear();

        Map<String, List<String>> valuesMap = model.getValuesMap();
        if (valuesMap != null) {
            server.getItems().addAll(valuesMap.get("servers"));
            port.getItems().addAll(valuesMap.get("ports"));
            database.getItems().addAll(valuesMap.get("databases"));
            schema.getItems().addAll(valuesMap.get("schemas"));
            user.getItems().addAll(valuesMap.get("users"));

            ConnectionParametersModel lastModel = model.getLatestSuccessfulConnection();
            if (lastModel != null) {
                server.setValue(lastModel.getServer());
                port.setValue(lastModel.getPort());
                database.setValue(lastModel.getDatabase());
                schema.setValue(lastModel.getSchema());
                user.setValue(lastModel.getUser());
                password.setText(lastModel.getPassword());
            }
        }
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

    private String composeReverseEngineeringParameters() {
        StringBuilder sb = new StringBuilder();

        sb.append("Database Type: ").append(targetDb.name()).append("\n");
        sb.append("Server: ").append(server.getSelectionModel().getSelectedItem()).append("\n");
        sb.append("Port: ").append(port.getSelectionModel().getSelectedItem()).append("\n");
        sb.append("Database: ").append(database.getSelectionModel().getSelectedItem()).append("\n");
        sb.append("User: ").append(user.getSelectionModel().getSelectedItem()).append("\n");

        return sb.toString();
    }
}
