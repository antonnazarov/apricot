package za.co.apricotdb.ui;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.comparator.CompareScriptGenerator;
import za.co.apricotdb.ui.comparator.CompareSnapshotRow;
import za.co.apricotdb.ui.handler.GenerateScriptHandler;
import za.co.apricotdb.ui.handler.SyntaxEditorHandler;

/**
 * The controller of the form apricot-generate-diff-script.fxml.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class CompareScriptController {

    @Autowired
    GenerateScriptHandler generateScriptHandler;

    @Autowired
    SyntaxEditorHandler syntaxEditorHandler;

    @Autowired
    CompareScriptGenerator compareScriptGenerator;

    @FXML
    Pane mainPane;

    @FXML
    RadioButton targetFile;

    @FXML
    RadioButton targetSqlEditor;

    @FXML
    ComboBox<String> schema;

    private List<CompareSnapshotRow> differences;

    private ToggleGroup targetGroup = new ToggleGroup();

    public enum ScriptTarget {
        FILE, SQL_EDITOR;
    }

    /**
     * Initialize the script generation form.
     */
    public void init(List<CompareSnapshotRow> differences) {
        this.differences = differences;

        targetFile.setToggleGroup(targetGroup);
        targetSqlEditor.setToggleGroup(targetGroup);
        targetSqlEditor.setSelected(true);

        List<String> schemaNames = generateScriptHandler.getSchemaNames();
        if (!schemaNames.isEmpty()) {
            schema.getItems().addAll(schemaNames);
        }
    }

    @FXML
    public void generate(ActionEvent event) {
        String scriptText = compareScriptGenerator.generate(differences, schema.getValue());
        switch (getScriptTarget()) {
        case FILE:
            generateScriptHandler.saveToFile("Generate Alignment Script", scriptText, mainPane.getScene().getWindow());
            break;
        case SQL_EDITOR:
            try {
                syntaxEditorHandler.createSyntaxEditorForm(scriptText, "The Snapshots Alignment Script");
            } catch (IOException e) {
                e.printStackTrace();
            }

            break;
        }
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    private ScriptTarget getScriptTarget() {
        ScriptTarget target = null;

        RadioButton btn = (RadioButton) targetGroup.getSelectedToggle();
        if (btn == targetFile) {
            target = ScriptTarget.FILE;
        } else {
            target = ScriptTarget.SQL_EDITOR;
        }

        return target;
    }
}
