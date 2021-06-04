package za.co.apricotdb.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import za.co.apricotdb.persistence.data.ProjectParameterManager;
import za.co.apricotdb.ui.handler.ExportDiagramToPdfHandler;
import za.co.apricotdb.ui.util.AlertMessageDecorator;
import za.co.apricotdb.ui.util.SaveFileHelper;

import java.io.File;

/**
 * This controller serves the form "Export to PDF".
 *
 * @author Anton Nazarov
 * @since 09/09/2020
 */
@Component
public class ExportDiagramToPdfController {

    @Autowired
    SaveFileHelper saveFileHelper;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ExportDiagramToPdfHandler exportDiagramToPdfHandler;

    @FXML
    TextField exportFileName;

    @FXML
    ChoiceBox<String> pageFormat;

    @FXML
    RadioButton portrait;

    @FXML
    RadioButton landscape;

    @FXML
    Pane mainPane;

    private ToggleGroup targetGroup = new ToggleGroup();

    public void init() {
        portrait.setToggleGroup(targetGroup);
        landscape.setToggleGroup(targetGroup);
        portrait.setSelected(true);

        pageFormat.getItems().clear();
        pageFormat.getItems().addAll("A4", "A3", "A2", "A1");
        pageFormat.getSelectionModel().select("A4");
    }

    @FXML
    public void getFileName() {
        File file = saveFileHelper.getFileToSave("Export Diagram into PDF","diagram.pdf",
                new FileChooser.ExtensionFilter("PDF documents", "*.pdf"), mainPane.getScene().getWindow(),
                ProjectParameterManager.EXPORT_PDF_OUTPUT_DIR);
        if (file != null) {
            exportFileName.setText(file.getPath());
        }
    }

    @FXML
    public void export() {
        String fileName = exportFileName.getText();
        if (StringUtils.isEmpty(fileName)) {
            Alert alert = alertDecorator.getAlert("Export Diagram",
                    "Please provide a name of the file", Alert.AlertType.WARNING);
            alert.showAndWait();
            return;
        }

        exportDiagramToPdfHandler.exportDiagram(fileName, pageFormat.getValue(), portrait.isSelected());
        getStage().close();
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}

