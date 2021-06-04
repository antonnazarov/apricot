package za.co.apricotdb.ui.util;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;

import java.io.File;

/**
 * The file name/location request form.
 *
 * @author Anton Nazarov
 * @since 06/09/2020
 */
@Component
public class SaveFileHelper {

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    /**
     * Request the file name to be saved.
     */
    public File getFileToSave(String title, String initialFileName, FileChooser.ExtensionFilter extensionFilter,
                              Window window, String outputDirParameter) {
        String outputDir = null;
        ApricotProjectParameter param = parameterManager.getParameterByName(projectManager.findCurrentProject(),
                outputDirParameter);
        if (param != null) {
            outputDir = param.getValue();
            File f = new File(outputDir);
            if (!f.isDirectory()) {
                outputDir = System.getProperty("user.dir");
            }
        } else {
            outputDir = System.getProperty("user.dir");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(outputDir));
        fileChooser.setInitialFileName(initialFileName);
        fileChooser.getExtensionFilters().add(extensionFilter);

        return fileChooser.showSaveDialog(window);
    }
}
