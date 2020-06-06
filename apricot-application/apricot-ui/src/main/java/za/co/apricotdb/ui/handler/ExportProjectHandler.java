package za.co.apricotdb.ui.handler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.support.export.ExportProjectProcessor;
import za.co.apricotdb.support.util.ApricotUtils;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This component is responsible for the gui-part of the export project
 * functionality.
 * 
 * @author Anton Nazarov
 * @since 20/03/2020
 */
@Component
public class ExportProjectHandler {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ExportProjectProcessor exportProcessor;

    @ApricotErrorLogger(title = "Unable to Export Project")
    public void exportProject(Window window) {
        ApricotProject project = projectManager.findCurrentProject();

        if (project != null) {
            ApricotProjectParameter param = parameterManager.getParameterByName(project,
                    ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR);
            String outputDir = System.getProperty("user.dir");
            if (param != null) {
                outputDir = param.getValue();
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Project");
            fileChooser.setInitialDirectory(new File(outputDir));
            fileChooser.setInitialFileName(exportProcessor.getDefaultProjectExportFileName(project.getName()));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(window);
            if (file != null) {
                String sProject = exportProcessor.serializeProject();
                try {
                    FileUtils.write(file, sProject, Charset.defaultCharset());
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
                Alert alert = alertDecorator.getAlert("Export Project",
                        "The Project was successfully exported into: " + file.getAbsolutePath(), AlertType.INFORMATION);
                alert.showAndWait();
                parameterManager.saveParameter(project, ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR,
                        file.getParent());
            }
        } else {
            Alert alert = alertDecorator.getErrorAlert("Export Project", "No Apricot DB Project is currently open");
            alert.show();
        }
    }
}
