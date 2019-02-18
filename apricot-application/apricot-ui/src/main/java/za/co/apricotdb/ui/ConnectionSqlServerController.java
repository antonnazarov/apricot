package za.co.apricotdb.ui;

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
import javafx.scene.control.Alert.AlertType;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.sqlserver.SqlServerUrlBuilder;
import za.co.apricotdb.ui.model.DatabaseConnectionModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

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
        applyModel(model);
    }

    @FXML
    public void testConnection(ActionEvent event) {
        String driverClass = sqlServerUrlBuilder.getDriverClass();
        String url = sqlServerUrlBuilder.getUrl(server.getSelectionModel().getSelectedItem(),
                port.getSelectionModel().getSelectedItem(), database.getSelectionModel().getSelectedItem());

        Alert alert = null;
        try {
            JdbcOperations op = MetaDataScanner.getTargetJdbcOperations(driverClass, url,
                    user.getSelectionModel().getSelectedItem(), password.getText());

            RowMapper<String> rowMapper = (rs, rowNum) -> {
                return rs.getString("name");
            };
            op.query("select name from sys.tables;", rowMapper);

            alert = getAlert(AlertType.INFORMATION, "The connection was successfully established");
        } catch (Exception e) {
            alert = getAlert(AlertType.ERROR, "Unable to connect to the database server:\n" + WordUtils.wrap(e.getMessage(), 60));
        }
        alert.showAndWait();
    }

    @FXML
    public void cancel(ActionEvent event) {

    }

    @FXML
    public void forward(ActionEvent event) {

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

}
