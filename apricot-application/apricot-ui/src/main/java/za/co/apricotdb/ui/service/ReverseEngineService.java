package za.co.apricotdb.ui.service;

import com.microsoft.sqlserver.jdbc.StringUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.controlsfx.dialog.ProgressDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.MetaDataScannerFactory;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.ParentWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The asynchronous reverse engineering service.
 *
 * @author Anton Nazarov
 * @since 01/09/2020
 */
@Component
public class ReverseEngineService extends Service<MetaData>  {

    private ObjectProperty<ApricotTargetDatabase> targetDatabase = new SimpleObjectProperty<>();
    private StringProperty driverClassName = new SimpleStringProperty();
    private StringProperty url = new SimpleStringProperty();
    private StringProperty schema = new SimpleStringProperty();
    private StringProperty userName = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private ObjectProperty<ApricotSnapshot> snapshot = new SimpleObjectProperty<>();
    private ProgressDialog progressDialog;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    MetaDataScannerFactory scannerFactory;

    public void initService(ApricotTargetDatabase targetDatabase, String driverClassName, String url, String schema,
                            String userName, String password, ApricotSnapshot snapshot) {
        this.targetDatabase.setValue(targetDatabase);
        this.driverClassName.setValue(driverClassName);
        this.url.setValue(url);
        this.schema.setValue(schema);
        this.userName.setValue(userName);
        this.password.setValue(password);
        this.snapshot.setValue(snapshot);

        initProgressDialog();

        this.reset();
    }

    public ApricotTargetDatabase getTargetDatabase() {
        return this.targetDatabase.getValue();
    }

    public String getDriverClassName() {
        return this.driverClassName.getValue();
    }

    public String getUrl() {
        return this.url.getValue();
    }

    public String getSchema() {
        return this.schema.getValue();
    }

    public String getUserName() {
        return this.userName.getValue();
    }

    public String getPassword() {
        return this.password.getValue();
    }

    public ApricotSnapshot getSnapshot() {
        return this.snapshot.getValue();
    }

    @Override
    protected Task<MetaData> createTask() {
        return new Task<>() {
            @Override
            protected MetaData call() {
                MetaDataScanner scanner = scannerFactory.getScanner(getTargetDatabase());

                if (StringUtils.isEmpty(getSchema())) {
                    String defSchema = scannerFactory.getDefaultSchema(getUrl(), getUserName(), getTargetDatabase());
                    if (StringUtils.isEmpty(defSchema)) {
                        schema = null;
                    } else {
                        schema = new SimpleStringProperty(defSchema);
                    }
                }

                updateMessage("Establishing connection...");
                JdbcOperations jdbc = MetaDataScanner.getTargetJdbcOperations(getDriverClassName(), getUrl(),
                        getUserName(), getPassword());
                updateProgress(1, 5);

                updateMessage("Scanning the tables...");
                Map<String, ApricotTable> tables = scanner.getTables(jdbc, getSnapshot(), getSchema());
                updateProgress(2, 5);

                updateMessage("Scanning the columns...");
                scanner.getColumns(jdbc, tables, getSchema());
                updateProgress(3, 5);

                updateMessage("Scanning the constraints...");
                Map<String, ApricotConstraint> constraints = scanner.getConstraints(jdbc, tables, getSchema());
                updateProgress(4, 5);

                updateMessage("Scanning the relationships...");
                List<ApricotRelationship> relationships = scanner.getRelationships(jdbc, constraints, getSchema());
                updateProgress(5, 5);

                MetaData ret = new MetaData();
                ret.setTables(new ArrayList<>(tables.values()));
                ret.setRelationships(relationships);

                return ret;
            }
        };
    }

    public ProgressDialog initProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.initOwner(parentWindow.getPrimaryStage());
        }
        progressDialog.setTitle("Reverse Engineering");
        progressDialog.setHeaderText("Scanning the database");

        return progressDialog;
    }
}
