package za.co.apricotdb.ui.handler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.MetaDataScannerFactory;
import za.co.apricotdb.persistence.data.DataSaver;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.ConnectionH2Controller;
import za.co.apricotdb.ui.ConnectionSqlServerController;
import za.co.apricotdb.ui.ConnectionSqliteController;
import za.co.apricotdb.ui.FiledbController;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.ReversedTablesController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.model.ConnectionAppParameterModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.ImageHelper;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This component is responsible for reverse engineering operation.
 */
@Component
public class ReverseEngineHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ConsistencyHandler consistencyHandler;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    DataSaver dataSaver;

    @Autowired
    MetaDataScannerFactory scannerFactory;

    @Autowired
    SqlServerParametersHandler parametersHandler;

    @Autowired
    DialogFormHandler formHandler;

    @Autowired
    ParentWindow pw;

    @Autowired
    ConnectionH2Controller h2Controller;

    @Autowired
    ConnectionSqliteController sqliteController;

    @ApricotErrorLogger(title = "Unable to start the Reverse Engineering process")
    public boolean startReverseEngineering() {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        if (tables != null && tables.size() > 0) {
            // the reverse eng operation cannot be performed of the non empty snapshot
            Alert alert = getAlert("The snapshot \"" + snapshot.getName() + "\" contains entities.\n"
                    + "You only can perform the database reverse engineering operation \ninto an EMPTY snapshot.");
            alert.showAndWait();

            return false;
        }

        try {
            openDatabaseConnectionForm(snapshot.getProject());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @ApricotErrorLogger(title = "Unable to open the result of the scan process")
    public void openScanResultForm(MetaData metaData, String[] blackList, String reverseEngineeringParameters) {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-re-tables-list.fxml",
                "bw-reverse-s1.jpg", "Result of the database scan");
        ReversedTablesController controller = form.getController();
        controller.init(metaData, blackList, reverseEngineeringParameters);

        form.show();
    }

    @ApricotErrorLogger(title = "Unable to save the reversed objects in the current Snapshot")
    public boolean saveReversedObjects(List<ApricotTable> included, List<ApricotTable> excluded,
                                       List<ApricotRelationship> relationships, String reverseEngineeringParameters) {
        Map<ApricotTable, ApricotTable> extraExclude = consistencyHandler.getFullConsistentExclude(excluded,
                relationships);
        if (!extraExclude.isEmpty()) {
            ButtonType yes = new ButtonType("Ok, exclude", ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(AlertType.WARNING, null, no, yes);
            alert.setTitle("Save results of the scan");
            alert.setHeaderText(WordUtils
                    .wrap("Some Parent tables were excluded from the resulting list. "
                            + "In order to maintain consistency of the scanned database structure, "
                            + "the corresponding Child tables will be excluded from the result:\n\n", 60)
                    + getMessageForExtraExclude(extraExclude));
            alertDecorator.decorateAlert(alert);
            alert.initOwner(pw.getWindow());
            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {
                // alter the input collections with the extra excluded values
                Set<ApricotTable> exclChildren = extraExclude.keySet();
                for (ApricotTable t : exclChildren) {
                    included.remove(t);
                    excluded.add(t);
                }

                // save the black list
                excluded.sort(Comparator.comparing(ApricotTable::getName));
                blackListHandler.saveBlackList(projectManager.findCurrentProject(), excluded);
            } else {
                return false;
            }
        }

        // request the relevant relationships
        List<ApricotRelationship> filteredRelationships = consistencyHandler.getRelationshipsForTables(included,
                relationships);

        // save tables and relationships
        MetaData md = new MetaData();
        md.setTables(included);
        md.setRelationships(filteredRelationships);
        dataSaver.saveMetaData(md);

        // save the extension of the snapshot comment
        setSnapshotReverseResultComment(reverseEngineeringParameters);

        return true;
    }

    @ApricotErrorLogger(title = "Unable to scan the database meta data")
    public MetaData getMetaData(ApricotTargetDatabase dbType, String driverClass, String url, String schema,
                                String user, String password, ApricotSnapshot snapshot) {
        MetaDataScanner scanner = scannerFactory.getScanner(dbType);
        MetaData metaData = scanner.scan(dbType, driverClass, url, schema, user, password, snapshot);

        return metaData;
    }

    @ApricotErrorLogger(title = "Unable to establish connection to the database", stop = true)
    public void testConnection(String server, String port, String database, String schema, String user, String password,
                               ApricotTargetDatabase targetDb, boolean integratedSecurity) {

        String driverClass = scannerFactory.getDriverClass(targetDb);
        String url = scannerFactory.getUrl(targetDb, server, port, database, integratedSecurity);

        JdbcOperations op = MetaDataScanner.getTargetJdbcOperations(driverClass, url, user, password);
        RowMapper<String> rowMapper = (rs, rowNum) -> {
            return "Connection test";
        };
        op.query(scannerFactory.getTestSQL(targetDb), rowMapper);

        // Success! Save the connection parameters in the project- parameter
        parametersHandler.saveConnectionParameters(targetDb.getDatabaseName(), server, port, database, schema, user, password);
    }

    private String getMessageForExtraExclude(Map<ApricotTable, ApricotTable> extraExclude) {
        StringBuilder sb = new StringBuilder();

        Set<ApricotTable> keys = extraExclude.keySet();
        List<ApricotTable> children = keys.stream().collect(Collectors.toList());
        Collections.sort(children, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        for (ApricotTable t : children) {
            sb.append(t.getName()).append(" [").append(extraExclude.get(t).getName()).append("]\n");
        }

        return sb.toString();
    }

    private Alert getAlert(String text) {
        Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle("Start Reverse Engineering process");
        alert.setHeaderText(text);
        alert.initOwner(pw.getWindow());
        alertDecorator.decorateAlert(alert);

        return alert;
    }

    private void openDatabaseConnectionForm(ApricotProject project) throws IOException {
        ApricotTargetDatabase targetDatabase = ApricotTargetDatabase.parse(project.getTargetDatabase());
        ConnectionAppParameterModel model = parametersHandler.getModel();

        Pane window = null;
        FXMLLoader loader = null;
        String title = null;
        switch (targetDatabase) {
            case MSSQLServer:
                title = "Connect to SQL Server database";
                window = initFormController(model);
                break;
            case Oracle:
                title = "Connect to Oracle database";
                window = initFormController(model);
                break;
            case PostrgeSQL:
                title = "Connect to PostgreSQL database";
                window = initFormController(model);
                break;
            case MySQL:
                title = "Connect to MySQL database";
                window = initFormController(model);
                break;
            case DB2:
                title = "Connect to DB2 (legacy) database";
                window = initFormController(model);
                break;
            case DB2_LUW:
                title = "Connect to DB2 (LUW) database";
                window = initFormController(model);
                break;
            case SQLite:
            case H2:
                loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-re-filedb.fxml"));
                loader.setControllerFactory(context::getBean);
                FiledbController controller = null;
                if (targetDatabase == ApricotTargetDatabase.H2) {
                    title = "Connect to H2 database";
                    controller = h2Controller;
                } else {
                    title = "Connect to SQLite database";
                    controller = sqliteController;
                }
                loader.setController(controller);
                window = loader.load();
                controller.init();
                break;
            default:
                break;
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pw.getPrimaryStage());
        dialog.setTitle(title);
        dialog.getIcons().add(ImageHelper.getImage("bw-reverse-s1.jpg", getClass()));
        Scene connectionScene = new Scene(window);
        dialog.setScene(connectionScene);
        connectionScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                dialog.close();
            }
        });

        dialog.show();
    }

    private void setSnapshotReverseResultComment(String reverseEngineeringParameters) {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder(snapshot.getComment());
        if (sb.length() > 0) {
            sb.append("\n\n");
        }
        sb.append(df.format(new java.util.Date())).append("->");
        sb.append(
                "The Reverse Engineering was successfully performed into this Snapshot with the following connection parameters:\n");
        sb.append(reverseEngineeringParameters);

        if (sb.length() < ApricotSnapshot.SNAPSHOT_COMMENT_LENGTH) {
            snapshot.setComment(sb.toString());
            snapshotManager.saveSnapshot(snapshot);
        }
    }

    private Pane initFormController(ConnectionAppParameterModel model) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/za/co/apricotdb/ui/apricot-re-sqlserver.fxml"));
        loader.setControllerFactory(context::getBean);
        Pane window = loader.load();

        ConnectionSqlServerController controller = loader.<ConnectionSqlServerController>getController();
        controller.init(model);

        return window;
    }
}
