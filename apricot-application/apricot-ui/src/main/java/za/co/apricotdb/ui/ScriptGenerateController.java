package za.co.apricotdb.ui;

import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The controller of the form apricot-generate-script.fxml.
 * 
 * @author Anton Nazarov
 * @since 02/04/2019
 */
@Component
public class ScriptGenerateController {

    @FXML
    Pane mainPane;

    @FXML
    RadioButton selectedOption;

    @FXML
    RadioButton currentViewOption;

    @FXML
    RadioButton snapshotOption;

    @FXML
    RadioButton targetFile;

    @FXML
    RadioButton targetSqlEditor;

    private DBScriptType scriptType;
    private ToggleGroup sourceGroup = new ToggleGroup();
    private ToggleGroup targetGroup = new ToggleGroup();

    enum ScriptSource {
        SELECTED, CURRENT_VIEW, CURRENT_SNAPSHOT;
    }

    enum ScriptTarget {
        FILE, SQL_EDITOR;
    }

    /**
     * Initialise the controller on startup.
     */
    public void init(DBScriptType scriptType, boolean hasSelected) {
        this.scriptType = scriptType;

        selectedOption.setToggleGroup(sourceGroup);
        currentViewOption.setToggleGroup(sourceGroup);
        snapshotOption.setToggleGroup(sourceGroup);
        if (!hasSelected) {
            selectedOption.setSelected(false);
            selectedOption.setDisable(true);
            currentViewOption.setSelected(true);
        } else {
            selectedOption.setSelected(true);
        }

        targetFile.setToggleGroup(targetGroup);
        targetSqlEditor.setToggleGroup(targetGroup);
        targetFile.setSelected(true);
    }

    private ScriptSource getScriptSource() {
        ScriptSource source = null;

        RadioButton btn = (RadioButton) sourceGroup.getSelectedToggle();
        if (btn == selectedOption) {
            source = ScriptSource.SELECTED;
        } else if (btn == currentViewOption) {
            source = ScriptSource.CURRENT_VIEW;
        } else {
            source = ScriptSource.CURRENT_SNAPSHOT;
        }

        return source;
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

    @FXML
    public void generate(ActionEvent event) {

        getStage().close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
