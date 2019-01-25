package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.model.ApricotViewSerializer;
import za.co.apricotdb.ui.model.ViewFormModel;
import za.co.apricotdb.ui.util.TextLimiter;

/**
 * This controller add new view.
 * 
 * @author Anton Nazarov
 * @since 23/01/2019
 *
 */
@Component
public class ViewFormController {
    
    @Autowired
    ApricotViewSerializer viewSerializer;

    @FXML
    TextField viewName;

    @FXML
    RadioButton initEmptyOption;

    @FXML
    RadioButton initSelectedOption;

    @FXML
    RadioButton initFromViewOption;

    @FXML
    ComboBox<String> fromViewList;

    @FXML
    ListView<String> availableTables;

    @FXML
    ListView<String> viewTables;

    @FXML
    Pane mainPane;
    
    @FXML
    TextArea comment;

    private Stage stage;

    private ViewFormModel model;

    /**
     * Initialize the form
     */
    public void init(ViewFormModel model) {
        this.model = model;
        TextLimiter.addTextLimiter(viewName, 30);
        TextLimiter.addTextLimiter(comment, 250);

        availableTables.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewTables.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if (model.isNewView()) {
            final ToggleGroup group = new ToggleGroup();
            initEmptyOption.setToggleGroup(group);
            initEmptyOption.setSelected(true);
            initSelectedOption.setToggleGroup(group);
            initFromViewOption.setToggleGroup(group);
            fromViewList.setDisable(true);
        } else {
            initEmptyOption.setDisable(true);
            initSelectedOption.setDisable(true);
            initFromViewOption.setDisable(true);
            fromViewList.setDisable(true);
        }

        applyModel(model);

        stage = (Stage) mainPane.getScene().getWindow();
    }

    @FXML
    public void ok(ActionEvent event) {
        model.setViewName(viewName.getText());
        model.setComment(comment.getText());
        
        if (!viewSerializer.validate(model)) {
            return;
        }
        
        viewSerializer.serializeView(model);
        stage.close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        stage.close();
    }

    @FXML
    public void addAllItems(ActionEvent event) {
        model.addAllItems();
        applyModel(model);
    }

    @FXML
    public void removeAllItems(ActionEvent event) {
        model.removeAllItems();
        applyModel(model);
    }

    @FXML
    public void addSelectedItems(ActionEvent event) {
        List<String> items = new ArrayList<>(availableTables.getSelectionModel().getSelectedItems());
        model.addSelectedItems(items);
        applyModel(model);
        for (String s : items) {
            viewTables.getSelectionModel().select(s);
        }
    }

    @FXML
    public void removeSelectedItems(ActionEvent event) {
        List<String> items = new ArrayList<>(viewTables.getSelectionModel().getSelectedItems());
        model.removeSelectedItems(items);
        applyModel(model);
        for (String s : items) {
            availableTables.getSelectionModel().select(s);
        }
    }

    private void applyModel(ViewFormModel model) {
        viewName.setText(model.getViewName());

        availableTables.getItems().clear();
        viewTables.getItems().clear();

        availableTables.getItems().addAll(model.getAvailableTables());
        viewTables.getItems().addAll(model.getViewTables());

        fromViewList.getItems().addAll(model.getFromViews());
    }
}
