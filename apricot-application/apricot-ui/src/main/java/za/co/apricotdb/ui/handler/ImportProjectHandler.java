package za.co.apricotdb.ui.handler;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.support.export.ImportProjectProcessor;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

import java.io.File;
import java.nio.charset.Charset;

/**
 * This component enclapsulates the functions for importing of the Apricot
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
        if (file != null) {
            importProject(file);
            parameterManager.saveParameter(project, ProjectParameterManager.PROJECT_DEFAULT_OUTPUT_DIR,
                    file.getParent());
            Alert alert = alertDecorator.getAlert("Import Project",
                    "The project \"" + project.getName() + "\" was successfully imported", Alert.AlertType.INFORMATION);
            alert.showAndWait();
        }
    }

    @ApricotErrorLogger(title = "Unable to Import Project")
    public void importProject(File file) {
        String sProject = null;
        try {
            sProject = FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        ApricotProject importedProject = importProcessor.deserializeProject(sProject);
        if (projectManager.getProjectByName(importedProject.getName()) != null) {
            Alert alert = alertDecorator.getErrorAlert("Import File", "The Project named \""
                    + importedProject.getName()
                    + "\" already exists in the system. You can delete or rename the existing project and then try to import again");
            alert.showAndWait();
            return;
        }

        ApricotProject project = importProcessor.importProject(sProject, true);

        //  the the just imported project as current
        projectManager.setProjectCurrent(project);
        applicationInitializer.initializeDefault();
    }
}
