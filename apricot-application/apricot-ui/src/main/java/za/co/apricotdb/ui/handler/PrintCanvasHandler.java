package za.co.apricotdb.ui.handler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This handler implements the printing functionality.
 *
 * @author Anton Nazarov
 * @since 02/09/2020
 */
@Component
public class PrintCanvasHandler {

    @Autowired
    ApricotCanvasHandler canvasHandler;

    @Autowired
    ParentWindow parentWindow;

    @ApricotErrorLogger(title = "Unable to print the current diagram")
    public void printCurrentCanvas() {

        ApricotCanvasImpl canvas = (ApricotCanvasImpl) canvasHandler.getSelectedCanvas();
        Stage owner = parentWindow.getPrimaryStage();

        WritableImage image = canvas.snapshot(null, null);
        File file = new File("c:/tmp/mytestimage.png");
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            PdfWriter writer = new PdfWriter("c:/tmp/mytestdoc.pdf");
            PdfDocument p = new PdfDocument(writer);
            Document document = new Document(p, PageSize.A4);
            ImageData idata = ImageDataFactory.create("c:/tmp/mytestimage.png");
            Image img = new Image(idata);
            document.add(img);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  printSetup(canvas, owner);
    }

    private void printSetup(Node node, Stage owner) {
        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null) {
            throw new IllegalArgumentException("Unable to create a new Print Job");
        }

        // Show the print setup dialog
        boolean proceed = job.showPrintDialog(owner);

        if (proceed) {
            print(job, node);
        }
    }

    private void print(PrinterJob job, Node node) {
        // Print the node
        boolean printed = job.printPage(node);

        if (printed) {
            job.endJob();
        }
    }
}
