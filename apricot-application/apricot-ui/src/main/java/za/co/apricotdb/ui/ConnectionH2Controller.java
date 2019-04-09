package za.co.apricotdb.ui;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import za.co.apricotdb.metascan.h2.H2Scanner;
import za.co.apricotdb.metascan.h2.H2UrlBuilder;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.ui.handler.BlackListHandler;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * The controller under the apricot-re-h2.fxml form.
 * 
 * @author Anton Nazarov
 * @since 08/04/2019
 */
@Component
public class ConnectionH2Controller {

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    H2UrlBuilder urlBuilder;

    @Autowired
    H2Scanner scanner;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

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

    @FXML
    public void openFile(ActionEvent event) {
        String outputDir = null;
        ApricotProjectParameter param = parameterManager.getParameterByName(projectManager.findCurrentProject(),
                ProjectParameterManager.H2DB_FILE_DEFAULT_DIR);
        if (param != null) {
            outputDir = param.getValue();
        } else {
            outputDir = System.getProperty("user.dir");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open H2 Database file");
        fileChooser.setInitialDirectory(new File(outputDir));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB2 Database", "*.mv.db");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(getStage());

        if (file != null) {
            fileName.setText(file.getPath());
            parameterManager.saveParameter(projectManager.findCurrentProject(),
                    ProjectParameterManager.H2DB_FILE_DEFAULT_DIR, file.getParent());
        }
    }

    @FXML
    public void forward(ActionEvent event) {
        if (fileName.getText() == null || fileName.getText().equals("")) {
            Alert alert = alertDecorator.getErrorAlert("Reverse H2 DB", "Please pick up the source H2-database file");
            alert.showAndWait();

            return;
        }

        String driverClass = urlBuilder.getDriverClass();
        String url = urlBuilder.getUrl(getH2DbName(fileName.getText()));
        MetaData metaData = scanner.scan(driverClass, url, schema.getText(), userName.getText(), password.getText(),
                snapshotManager.getDefaultSnapshot());
        String[] blackList = blackListHandler.getBlackListTables(projectManager.findCurrentProject());
        try {
            getStage().close();
            reverseEngineHandler.openScanResultForm(metaData, blackList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
    
    private String getH2DbName(String path) {
        int pos = path.indexOf("mv.db");
        if (pos != -1) {
            return path.substring(0, pos);
        }
        
        return null;
        
    }
}
