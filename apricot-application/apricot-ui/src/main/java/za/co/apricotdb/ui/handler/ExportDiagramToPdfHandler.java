package za.co.apricotdb.ui.handler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.ui.ExportDiagramToPdfController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Export the current Diagram into the PDF document.
 *
 * @author Anton Nazarov
 * @since 06/09/2020
 */
@Component
public class ExportDiagramToPdfHandler {

    @Autowired
    DialogFormHandler formHandler;

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ProjectParameterManager parameterManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @ApricotErrorLogger(title = "Unable to open the Export Diagram to PDF screen")
    public void openSearchForm() {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-export-to-pdf.fxml",
                "view-s1.jpg", "Export Diagram to PDF");
        ExportDiagramToPdfController controller = form.getController();
        controller.init();

        form.show();
    }

    @ApricotErrorLogger(title = "Unable to export the current diagram into the PDF document")
    public void exportDiagram(String fileName, String pageFormat, boolean portrait) {
        PageSize pageSize = getPageSize(pageFormat);
        if (!portrait) {
            pageSize = pageSize.rotate();
        }
        ApricotCanvasImpl canvas = (ApricotCanvasImpl) canvasHandler.getSelectedCanvas();
        WritableImage image = canvas.snapshot(null, null);
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            byte[] bytes = outputStream.toByteArray();

            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument p = new PdfDocument(writer);
            Document document = new Document(p, pageSize);
            ImageData imgData = ImageDataFactory.create(bytes);
            Image img = new Image(imgData);
            document.add(img);
            document.close();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }

        File file = new File(fileName);
        Alert alert = alertDecorator.getAlert("Export Diagram",
                "The current Diagram was successfully exported into: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        alert.showAndWait();

        parameterManager.saveParameter(projectManager.findCurrentProject(),
                ProjectParameterManager.EXPORT_PDF_OUTPUT_DIR, file.getParent());
    }

    private PageSize getPageSize(String size) {
        PageSize pageSize = PageSize.A4;
        switch (size) {
            case "A4":
                pageSize = PageSize.A4;
                break;
            case "A3":
                pageSize = PageSize.A3;
                break;
            case "A2":
                pageSize = PageSize.A2;
                break;
            case "A1":
                pageSize = PageSize.A1;
                break;
        }

        return pageSize;
    }
}
