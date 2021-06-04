package za.co.apricotdb.ui.handler;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.SaveFileHelper;
import za.co.apricotdb.viewport.align.CanvasSizeAdjustor;
import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Export the current Diagram into the PNG format (picture).
 *
 * @author Anton Nazarov
 * @since 06/09/2020
 */
@Component
public class ExportDiagramToPictureHandler {

    @Autowired
    SaveFileHelper saveFileHelper;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    @ApricotErrorLogger(title = "Unable to export the current diagram into the PNG- file")
    public void exportDiagram() {
        ApricotCanvasImpl canvas = (ApricotCanvasImpl) canvasHandler.getSelectedCanvas();
        Stage owner = parentWindow.getPrimaryStage();
        File file = saveFileHelper.getFileToSave("Export Diagram into Picture","diagram.png",
                new FileChooser.ExtensionFilter("PNG files", "*.png"), owner,
                ProjectParameterManager.EXPORT_PICTURE_OUTPUT_DIR);
        if (file != null) {
            CanvasSizeAdjustor adjustor = new CanvasSizeAdjustor(canvas);
            adjustor.alignCanvasSize();
            WritableImage image = canvas.snapshot(null, null);
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }

            Alert alert = alertDecorator.getAlert("Export Project",
                    "The current Diagram was successfully exported into: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            alert.showAndWait();
            parameterManager.saveParameter(projectManager.findCurrentProject(), ProjectParameterManager.EXPORT_PICTURE_OUTPUT_DIR,
                    file.getParent());

            Application app = parentWindow.getApplication();
            app.getHostServices().showDocument(file.getAbsolutePath());
        }
    }
}
