package za.co.apricotdb.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.oracle.OracleServiceType;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.ui.handler.SqlServerParametersHandler;
import za.co.apricotdb.ui.model.ConnectionAppParameterModel;
import za.co.apricotdb.ui.model.ConnectionParametersModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This controller served the form apricot-re-oracle.fxml
 *
 * @author Anton Nazarov
 * @since 23/05/2021
 */
@Component
public class ConnectionOracleController {

    @FXML
    Pane mainPane;

    @FXML
    RadioButton connectionTypeSid;

    @FXML
    RadioButton connectionTypeService;

    @FXML
    RadioButton connectionTypeTns;

    @FXML
    ComboBox<String> serviceName;

    @FXML
    TextField pathToTnsnamesOraFile;

    @FXML
    Button pathToTnsnamesOraFileButton;

    @FXML
    ComboBox<String> server;

    @FXML
    ComboBox<String> port;

    @FXML
    ComboBox<String> schema;

    @FXML
    ComboBox<String> user;

    @FXML
    PasswordField password;

    @Autowired
    SqlServerParametersHandler parametersHandler;

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    AlertMessageDecorator alertHandler;

    private final ToggleGroup serviceTypeGroup = new ToggleGroup();
    private OracleServiceType serviceType;

    /**
     * Initialize the Oracle connection form.
     */
    public void init(ConnectionAppParameterModel model) {
        //  initialize the Service Types toggle group
        connectionTypeSid.setToggleGroup(serviceTypeGroup);
        connectionTypeService.setToggleGroup(serviceTypeGroup);
        connectionTypeTns.setToggleGroup(serviceTypeGroup);

        connectionTypeTns.setOnAction(e -> {
            setTnsType(connectionTypeTns.isSelected());
            serviceType = OracleServiceType.TNS;
        });

        connectionTypeSid.setOnAction(e -> {
            setTnsType(connectionTypeTns.isSelected());
            serviceType = OracleServiceType.SID;
        });

        connectionTypeService.setOnAction(e -> {
            setTnsType(connectionTypeTns.isSelected());
            serviceType = OracleServiceType.SERVICE;
        });

        applyModel(model);
    }

    @FXML
    public void testConnection() {
        reverseEngineHandler.testConnection(server.getValue(), port.getValue(), serviceName.getValue(), schema.getValue(), user.getValue(),
                password.getText(), ApricotTargetDatabase.Oracle, false, serviceType, pathToTnsnamesOraFile.getText());
        Alert alert = alertHandler.getAlert("Test Connection", "The connection was successfully established",
                Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    @FXML
    public void forward() {
        parametersHandler.saveConnectionParameters(ApricotTargetDatabase.Oracle.name(), server.getValue(),
                port.getValue(), serviceName.getValue(), schema.getValue(), user.getValue(), password.getText(), serviceType.name(),
                pathToTnsnamesOraFile.getText());
    }

    /**
     * Handle the button click event on the select dir button
     */
    @FXML
    public void selectPathToTnsnamesOraFile() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (StringUtils.isNotEmpty(pathToTnsnamesOraFile.getText())) {
            File f = new File(pathToTnsnamesOraFile.getText());
            if (f.isDirectory()) {
                directoryChooser.setInitialDirectory(new File(pathToTnsnamesOraFile.getText()));
            }
        }
        File selectedDirectory = directoryChooser.showDialog(getStage());
        if (selectedDirectory != null) {
            pathToTnsnamesOraFile.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    private void setTnsType(boolean setTnsOn) {
        if (setTnsOn) {
            server.setDisable(true);
            port.setDisable(true);
            pathToTnsnamesOraFile.setDisable(false);
            pathToTnsnamesOraFileButton.setDisable(false);
        } else {
            server.setDisable(false);
            port.setDisable(false);
            pathToTnsnamesOraFile.setDisable(true);
            pathToTnsnamesOraFileButton.setDisable(true);
        }
    }

    private void applyModel(ConnectionAppParameterModel model) {
        serviceName.getItems().clear();
        server.getItems().clear();
        port.getItems().clear();
        schema.getItems().clear();
        user.getItems().clear();

        serviceType = OracleServiceType.SERVICE;
        Map<String, List<String>> valuesMap = model.getValuesMap();
        if (valuesMap != null) {
            serviceName.getItems().addAll(valuesMap.get("databases"));
            server.getItems().addAll(valuesMap.get("servers"));
            port.getItems().addAll(valuesMap.get("ports"));
            schema.getItems().addAll(valuesMap.get("schemas"));
            user.getItems().addAll(valuesMap.get("users"));

            ConnectionParametersModel lastModel = model.getLatestSuccessfulConnection();
            if (lastModel != null) {
                if (StringUtils.isNotEmpty(lastModel.getServiceType())) {
                    serviceType = OracleServiceType.valueOf(lastModel.getServiceType());
                }

                serviceName.setValue(lastModel.getDatabase());
                server.setValue(lastModel.getServer());
                port.setValue(lastModel.getPort());
                schema.setValue(lastModel.getSchema());
                user.setValue(lastModel.getUser());
                password.setText(lastModel.getPassword());
                pathToTnsnamesOraFile.setText(lastModel.getTnsNamesOraPath());
            }
        }

        switch (serviceType) {
            case SERVICE:
                connectionTypeService.setSelected(true);
                setTnsType(false);
                break;
            case SID:
                connectionTypeSid.setSelected(true);
                setTnsType(false);
                break;
            case TNS:
                connectionTypeTns.setSelected(true);
                setTnsType(true);
                break;
        }
    }
}
