package za.co.apricotdb.ui;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import za.co.apricotdb.syntaxtext.SyntaxTextAreaFX;
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
    VBox elementHolder; 

    SyntaxTextAreaFX textEditor;

    private String operationName;

    public void init(String script, String operationName) {
        textEditor = new SyntaxTextAreaFX();
        textEditor.setPrefSize(600, 600);
        textEditor.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");
        textEditor.setText(script);
        VirtualizedScrollPane<SyntaxTextAreaFX> scroll = new VirtualizedScrollPane<>(textEditor);
        elementHolder.getChildren().remove(0);
        elementHolder.getChildren().add(0, scroll);
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
