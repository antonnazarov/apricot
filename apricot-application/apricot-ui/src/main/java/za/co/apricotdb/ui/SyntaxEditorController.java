package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.GenerateScriptHandler;

/**
 * The controller serves the form apricot-syntax-editor.fxml.
 * 
 * @author Anton Nazarov
 * @since 03/03/2019
 */
@Component
public class SyntaxEditorController {

    @Autowired
    GenerateScriptHandler generateScriptHandler;

    @FXML
    Pane mainPane;

    @FXML
    TextArea textEditor;

    private String operationName;

    public void init(String script, String operationName) {
        textEditor.setText(script);
        this.operationName = operationName;
    }

    @FXML
    public void saveAs(ActionEvent event) throws Exception {
        generateScriptHandler.saveToFile(operationName, textEditor.getText(), getStage());
    }

    @FXML
    public void cancel(ActionEvent event) throws Exception {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
