package za.co.apricotdb.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.DatabaseUrlBuilder;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.ui.handler.BlackListHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.ui.handler.SqlServerParametersHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;
import java.util.Properties;

/**
 * The controller under the apricot-re-filedb.fxml form.
 * It serves the connections to the file databases, currently - H2 and SQLite.
 *
 * @author Anton Nazarov
 * @since 08/04/2019
 */
public abstract class AbstractFiledbController implements FiledbController {

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    SqlServerParametersHandler parametersHandler;

    @FXML
    Pane mainPane;

    @FXML
    TextField fileName;

    @FXML
    TextField schema;

    @FXML
    TextField userName;

    @FXML
    PasswordField password;

    private ApricotSnapshot snapshot;
    private ApricotProject project;
    MetaDataScanner scanner;
    DatabaseUrlBuilder urlBuilder;
    ApricotTargetDatabase target;

    @Override
    public void init() {
        project = projectManager.findCurrentProject();
        snapshot = snapshotManager.getDefaultSnapshot();

        Properties props = parametersHandler.getLatestConnectionProperties();
        if (props != null) {
            String sFileName = props.getProperty(ProjectParameterManager.CONNECTION_SERVER);
            if (StringUtils.isNotEmpty(sFileName) && doesFileExist(sFileName, false)) {
                fileName.setText(sFileName);
            }
            String sSchema = props.getProperty(ProjectParameterManager.CONNECTION_SCHEMA);
            if (sSchema != null) {
                schema.setText(sSchema);
            }
            String sUser = props.getProperty(ProjectParameterManager.CONNECTION_USER);
            if (sUser != null) {
                userName.setText(sUser);
            }
        }

        initScanner();
    }

    @FXML
    public void openFile() {
        String outputDir = null;
        ApricotProjectParameter param = parameterManager.getParameterByName(projectManager.findCurrentProject(),
                ProjectParameterManager.H2DB_FILE_DEFAULT_DIR);

        if (param != null && StringUtils.isNotEmpty(param.getValue()) && doesFileExist(param.getValue(), true)) {
            outputDir = param.getValue();
        } else {
            outputDir = System.getProperty("user.dir");
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = null;
        fileChooser.setTitle(getFileChooserTitle());
        extFilter = getExtensionFilter();

        fileChooser.setInitialDirectory(new File(outputDir));
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(getStage());

        if (file != null) {
            fileName.setText(file.getPath());
            parameterManager.saveParameter(projectManager.findCurrentProject(),
                    ProjectParameterManager.H2DB_FILE_DEFAULT_DIR, file.getParent());
        }
    }

    @FXML
    public void forward() {
        if (fileName.getText() == null || fileName.getText().equals("")) {
            Alert alert = alertDecorator.getErrorAlert("Reverse H2 DB", "Please pick up the source H2-database file");
            alert.showAndWait();

            return;
        }

        if (!doesFileExist(fileName.getText(), false)) {
            Alert alert = alertDecorator.getErrorAlert("Reverse " + target.name(),
                    "Unable to find the file: " + fileName.getText());
            alert.showAndWait();

            return;
        }

        String driverClass = urlBuilder.getDriverClass();
        String url = urlBuilder.getUrl(getFileName(fileName.getText()), null, null, false);
        MetaData metaData = reverseEngineHandler.getMetaData(target, driverClass, url,
                schema.getText(), userName.getText(), password.getText(), snapshot);
        String[] blackList = blackListHandler.getBlackListTables(project);

        getStage().close();
        reverseEngineHandler.openScanResultForm(metaData, blackList, composeReverseEngineeringParameters());

        // save the parameters filed in the form
        parametersHandler.saveConnectionParameters("H2", fileName.getText(), "N/A", "N/A", schema.getText(),
                userName.getText(), password.getText());
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    private String composeReverseEngineeringParameters() {
        StringBuilder sb = new StringBuilder();

        sb.append("Database Type: ").append(target.name()).append("\n");
        sb.append("Database file: ").append(fileName.getText()).append("\n");
        sb.append("Schema: ").append(schema.getText()).append("\n");
        sb.append("User: ").append(userName.getText()).append("\n");

        return sb.toString();
    }

    private boolean doesFileExist(String filePath, boolean isDirectory) {
        File f = new File(filePath);
        return f.exists() && (f.isDirectory() == isDirectory);
    }
}
