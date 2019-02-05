package za.co.apricotdb.ui;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.ui.handler.ApricotCanvasHandler;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.model.ApricotViewSerializer;
import za.co.apricotdb.ui.model.NewViewModelBuilder;
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

    @Autowired
    ApricotViewHandler viewHandler;

    @Autowired
    NewViewModelBuilder newViewModelBuilder;

    @Autowired
    ApricotCanvasHandler canvasHandler;

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
    private TabPane viewsTabPane;
    private PropertyChangeListener canvasChangeListener;

    /**
     * Initialize the form
     */
    public void init(ViewFormModel model, TabPane viewsTabPane, PropertyChangeListener canvasChangeListener) {
        this.model = model;
        this.viewsTabPane = viewsTabPane;
        this.canvasChangeListener = canvasChangeListener;
        TextLimiter.addTextLimiter(viewName, 30);
        TextLimiter.addTextLimiter(comment, 250);

        availableTables.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewTables.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        fromViewList.getItems().clear();
        fromViewList.getItems().add("<none>");
        fromViewList.getItems().addAll(model.getFromViews());
        fromViewList.getSelectionModel().select(0);

        if (model.isNewView()) {
            final ToggleGroup group = new ToggleGroup();
            initEmptyOption.setToggleGroup(group);
            initSelectedOption.setToggleGroup(group);
            initFromViewOption.setToggleGroup(group);
            if (model.getSelectedInCanvas() == null || model.getSelectedInCanvas().size() == 0) {
                initSelectedOption.setDisable(true);
                initEmptyOption.setSelected(true);
            } else {
                initSelectedOption.setSelected(true);
            }
        } else {
            initEmptyOption.setDisable(true);
            initSelectedOption.setDisable(true);
            initFromViewOption.setDisable(true);
        }
        fromViewList.setDisable(true);

        applyModel(model);

        stage = (Stage) mainPane.getScene().getWindow();
    }

    @FXML
    public void save(ActionEvent event) {
        setModelValues();

        if (!viewSerializer.validate(model)) {
            return;
        }

        ApricotView view = viewSerializer.serializeView(model);

        if (model.isNewView()) {
            Tab tab = viewHandler.createViewTab(model.getSnapshot(), view, viewsTabPane, canvasChangeListener);
            viewsTabPane.getSelectionModel().select(tab);
        } else {
            model.getTab().setText(viewName.getText());
            if (model.getTabInfo() != null) {
                model.getTabInfo().setView(view);
                canvasHandler.populateCanvas(model.getSnapshot(), view, model.getTabInfo().getCanvas());
            }
        }

        stage.close();
    }

    private void setModelValues() {
        model.setViewName(viewName.getText());
        model.setComment(comment.getText());
    }

    @FXML
    public void cancel(ActionEvent event) {
        stage.close();
    }

    @FXML
    public void addAllItems(ActionEvent event) {
        setModelValues();
        model.addAllItems();
        applyModel(model);
    }

    @FXML
    public void removeAllItems(ActionEvent event) {
        setModelValues();
        model.removeAllItems();
        applyModel(model);
    }

    @FXML
    public void addSelectedItems(ActionEvent event) {
        setModelValues();
        List<String> items = new ArrayList<>(availableTables.getSelectionModel().getSelectedItems());
        model.addSelectedItems(items);
        applyModel(model);
        for (String s : items) {
            viewTables.getSelectionModel().select(s);
        }
    }

    @FXML
    public void removeSelectedItems(ActionEvent event) {
        setModelValues();
        List<String> items = new ArrayList<>(viewTables.getSelectionModel().getSelectedItems());
        model.removeSelectedItems(items);
        applyModel(model);
        for (String s : items) {
            availableTables.getSelectionModel().select(s);
        }
    }

    /**
     * The "empty" option has been selected.
     */
    @FXML
    public void selectEmpty(ActionEvent event) {
        removeAllItems(event);
        disableFromViewList();
    }

    /**
     * The "selected" option has been selected.
     */
    @FXML
    public void selectSelected(ActionEvent event) {
        List<String> availableTables = newViewModelBuilder
                .getAvailableTablesFromSnapshotAndSelected(model.getSnapshotTables(), model.getSelectedInCanvas());
        model.getAvailableTables().clear();
        model.addAvailableTables(availableTables);
        model.getViewTables().clear();
        model.addViewTables(model.getSelectedInCanvas());

        applyModel(model);

        disableFromViewList();
    }

    private void disableFromViewList() {
        fromViewList.getSelectionModel().select(0);
        fromViewList.setDisable(true);
    }

    /**
     * The "From View" option.
     */
    @FXML
    public void selectView(ActionEvent event) {
        fromViewList.setDisable(false);
    }

    private void applyModel(ViewFormModel model) {
        viewName.setText(model.getViewName());
        comment.setText(model.getComment());

        availableTables.getItems().clear();
        viewTables.getItems().clear();

        availableTables.getItems().addAll(model.getAvailableTables());
        viewTables.getItems().addAll(model.getViewTables());
    }

    @FXML
    public void viewSelectedFromList(ActionEvent event) {
        String viewName = fromViewList.getSelectionModel().getSelectedItem();
        ApricotView view = viewHandler.getViewByName(model.getSnapshot().getProject(), viewName);

        List<ApricotTable> tables = viewHandler.getTablesForView(model.getSnapshot(), view);
        List<String> tableNames = new ArrayList<>();
        for (ApricotTable t : tables) {
            tableNames.add(t.getName());
        }
        Collections.sort(tableNames);

        List<String> availableTables = newViewModelBuilder
                .getAvailableTablesFromSnapshotAndSelected(model.getSnapshotTables(), tableNames);
        model.getAvailableTables().clear();
        model.addAvailableTables(availableTables);
        model.getViewTables().clear();
        model.addViewTables(tableNames);
        
        model.setViewName(this.viewName.getText());
        model.setComment(this.comment.getText());

        applyModel(model);
    }
}
