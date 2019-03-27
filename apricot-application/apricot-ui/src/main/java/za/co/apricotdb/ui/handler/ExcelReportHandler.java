package za.co.apricotdb.ui.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import za.co.apricotdb.persistence.data.DataReader;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.support.excel.ReportWriter;
import za.co.apricotdb.support.excel.TableWrapper;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This handler helps to create the Excel report.
 * 
 * @author Anton Nazarov
 * @since 27/03/2019
 */
@Component
public class ExcelReportHandler {

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    TableManager tableManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    DataReader dataReader;

    @Autowired
    ReportWriter reportWriter;

    @Transactional
    public void createExcelReport(Window window) {
        ApricotSnapshot snapshot = snapshotManager.getDefaultSnapshot();
        if (!checkNonEmptySnapshot(snapshot)) {
            return;
        }

        String outputDir = null;
        ApricotProjectParameter param = parameterManager.getParameterByName(projectManager.findCurrentProject(),
                ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR);
        if (param != null) {
            outputDir = param.getValue();
        } else {
            outputDir = System.getProperty("user.dir");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Create Excel Report");
        fileChooser.setInitialDirectory(new File(outputDir));
        fileChooser.setInitialFileName("snap_" + snapshot.getName());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MS Excel files", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try {
                createReport(file.getPath(), snapshot.getId());
                Alert alert = alertDecorator.getAlert("Excel Report",
                        "The Excel Report was successfully created in: " + file.getAbsolutePath(),
                        AlertType.INFORMATION);
                alert.showAndWait();
                parameterManager.saveParameter(projectManager.findCurrentProject(),
                        ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR, file.getParent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkNonEmptySnapshot(ApricotSnapshot snapshot) {
        List<ApricotTable> tables = tableManager.getTablesForSnapshot(snapshot);
        if (tables.size() == 0) {
            Alert alert = alertDecorator.getErrorAlert("Excel Report", "The Snapshot \"" + snapshot.getName()
                    + "\" seems to be empty. To create the Excel- report there should be some tables in the Snapshot");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    private void createReport(String fileName, long snapshotId) throws Exception {
        List<String> l = new ArrayList<>();
        l.add("*");
        MetaData result = dataReader.readTablesByList(l, snapshotId);
        List<ApricotTable> tables = result.getTables();
        tables.sort((ApricotTable t1, ApricotTable t2) -> t1.getName().compareTo(t2.getName()));
        List<TableWrapper> wrappers = new ArrayList<>();
        for (ApricotTable t : tables) {
            wrappers.add(new TableWrapper(t, result.getRelationships()));
        }

        reportWriter.createReport(wrappers, fileName);
    }
}
