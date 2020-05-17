package za.co.apricotdb.ui.handler;

import java.io.File;
import java.nio.charset.Charset;

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
import za.co.apricotdb.support.export.ImportProjectProcessor;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This component inclapsulates the functions for importing of the Aprocit
 * Project.
 *
 * @author Anton Nazarov
 * @since 21/03/2020
 */
@Component
public class ImportProjectHandler {

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ImportProjectProcessor importProcessor;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @ApricotErrorLogger(title = "Unable to Import Project")
    public void importProject(Window window) {
        ApricotProject project = projectManager.findCurrentProject();
        ApricotProjectParameter param = null;
        if (project != null) {
            param = parameterManager.getParameterByName(project, ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR);
        }
        String outputDir = System.getProperty("user.dir");
        if (param != null) {
            outputDir = param.getValue();
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Project");
        fileChooser.setInitialDirectory(new File(outputDir));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(window);
        String sProject = null;
        if (file != null) {
            importProject(file);
        }

        parameterManager.saveParameter(project, ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR,
                file.getParent());
    }

    @ApricotErrorLogger(title = "Unable to Import Project")
    public void importProject(File file) {
        String sProject = null;
        try {
            sProject = FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        ApricotProject importedProj = importProcessor.deserializeProject(sProject);
        if (projectManager.getProjectByName(importedProj.getName()) != null) {
            Alert alert = alertDecorator.getErrorAlert("Import File", "The Project named \""
                    + importedProj.getName()
                    + "\" already exists in the system. You can delete or rename the existing project and then try to import again");
            alert.showAndWait();
        }

        ApricotProject project = importProcessor.importProject(sProject, true);

        Alert alert = alertDecorator.getAlert("Import Project",
                "The project \"" + importedProj.getName() + "\" was successfully imported", AlertType.INFORMATION);
        alert.showAndWait();

        //  the the just imported project as current
        projectManager.setProjectCurrent(project);
        applicationInitializer.initializeDefault();
    }
}
