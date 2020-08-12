package za.co.apricotdb.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.syntaxtext.SyntaxTextAreaFX;
import za.co.apricotdb.ui.handler.AdvancedSearchHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * The controller of the advanced search form.
 *
 * @author Anton Nazarov
 * @since 12/08/2020
 */
@Component
public class AdvancedSearchController {

    @Autowired
    AdvancedSearchHandler searchHandler;

    @FXML
    Pane mainPane;

    @FXML
    TableView<AdvancedSearchResultRow> resultTable;

    @FXML
    TableColumn<AdvancedSearchResultRow, String> entityName;

    @FXML
    TableColumn<AdvancedSearchResultRow, CheckBox> entitySelected;

    @FXML
    Button searchButton;

    @FXML
    Button cleanResultButton;

    @FXML
    Button selectAllButton;

    @FXML
    Button unselectAllButton;

    @FXML
    Label statusBar;

    @FXML
    HBox editorHolder;

    private SyntaxTextAreaFX textEditor;
    private ObservableList<AdvancedSearchResultRow> resultList;

    /**
     * Initialize the form on open.
     */
    public void init(String text) {
        textEditor = new SyntaxTextAreaFX();
        textEditor.setStyle("-fx-border-color: lightgray; -fx-padding: 5;");
        HBox.setHgrow(textEditor, Priority.ALWAYS);
        if (text != null) {
            textEditor.setText(text);
        }
        VirtualizedScrollPane<SyntaxTextAreaFX> scroll = new VirtualizedScrollPane<>(textEditor);
        HBox.setHgrow(scroll, Priority.ALWAYS);
        editorHolder.getChildren().remove(0);
        editorHolder.getChildren().add(0, scroll);

        textEditor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (StringUtils.isNotEmpty(newValue)) {
                searchButton.setDisable(false);
            } else {
                searchButton.setDisable(true);
            }
        });

        if (StringUtils.isNotEmpty(text)) {
            searchButton.setDisable(false);
        } else {
            searchButton.setDisable(true);
        }

        selectAllButton.setDisable(true);
        unselectAllButton.setDisable(true);
        cleanResultButton.setDisable(true);

        entityName.setCellValueFactory(new PropertyValueFactory<AdvancedSearchResultRow, String>("entityName"));
        entitySelected.setCellValueFactory(new PropertyValueFactory<AdvancedSearchResultRow, CheckBox>("entitySelected"));
        entitySelected.setStyle("-fx-alignment: CENTER;");

        statusBar.setText("");
    }

    @FXML
    public void search() {
        resultTable.getItems().clear();

        if (StringUtils.isNotEmpty(textEditor.getText())) {
            List<String> tables = searchHandler.findEntitiesInFreeText(textEditor.getText());

            if (!tables.isEmpty()) {
                List<AdvancedSearchResultRow> list = new ArrayList<>();
                for (String table : tables) {
                    list.add(new AdvancedSearchResultRow(table, this));
                }
                resultList = FXCollections.observableList(list);
                resultTable.setItems(resultList);

                selectAllButton.setDisable(false);
                unselectAllButton.setDisable(false);
                cleanResultButton.setDisable(false);

                searchHandler.savePreviousSearchCondition(textEditor.getText());
            } else {
                selectAllButton.setDisable(true);
                unselectAllButton.setDisable(true);
                cleanResultButton.setDisable(true);
            }
        }

        buildStatusBar();
    }

    @FXML
    public void cleanResult() {
        resultList.clear();
        buildStatusBar();
    }

    @FXML
    public void selectAll() {
        for (AdvancedSearchResultRow r : resultList) {
            r.getEntitySelected().setSelected(true);
        }
        buildStatusBar();
    }

    @FXML
    public void unselectAll() {
        for (AdvancedSearchResultRow r : resultList) {
            r.getEntitySelected().setSelected(false);
        }
        buildStatusBar();
    }

    @FXML
    public void close() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    public void buildStatusBar() {
        if (resultList.isEmpty()) {
            statusBar.setText("No Entities in the result");
        } else {
            int selected = 0;
            for (AdvancedSearchResultRow r : resultList) {
                if (r.getEntitySelected().isSelected()) {
                    selected++;
                }
            }
            statusBar.setText("Found: " + resultList.size() + " Entities; Selected: " + selected + " Entities");
        }
    }
}
