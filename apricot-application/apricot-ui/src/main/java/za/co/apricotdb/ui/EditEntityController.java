package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import za.co.apricotdb.persistence.data.ApplicationParameterManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.ui.model.ApricotColumnData;
import za.co.apricotdb.ui.model.EditEntityModel;

/**
 * This controller is allocated under the apricot-entity-editor.fxml form.
 * 
 * @author Anton Nazarov
 * @since 26/02/2019
 */
@Component
public class EditEntityController {

    @Autowired
    ApplicationParameterManager appParameterManager;

    @Autowired
    ProjectManager projectManager;

    @FXML
    Pane mainPane;

    @FXML
    TextField entityName;

    @FXML
    TableView<ApricotColumnData> columnDefinitionTable;

    @FXML
    TableColumn<ApricotColumnData, String> columnName;

    @FXML
    TableColumn<ApricotColumnData, Boolean> primaryKey;

    @FXML
    TableColumn<ApricotColumnData, Boolean> allowsNull;

    @FXML
    TableColumn<ApricotColumnData, String> dataType;

    @FXML
    TableColumn<ApricotColumnData, String> length;

    @FXML
    TableColumn<ApricotColumnData, String> comment;

    private EditEntityModel model;

    public void init(EditEntityModel model) {
        this.model = model;

        columnDefinitionTable.getSelectionModel().cellSelectionEnabledProperty().set(true);
        columnDefinitionTable.setOnKeyPressed(e -> {
            if (e.getCode().isLetterKey() || e.getCode().isDigitKey()) {
                editFocusedCell();
            }
        });

        columnName.setCellValueFactory(e -> e.getValue().getName());
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnName.setOnEditCommit(e -> {
            TablePosition<ApricotColumnData, String> pos = e.getTablePosition();
            model.getColumns().get(pos.getRow()).getName().setValue(e.getNewValue());
            columnDefinitionTable.getSelectionModel().selectRightCell();
        });

        primaryKey.setCellValueFactory(e -> e.getValue().getPrimaryKey());
        primaryKey.setCellFactory(CheckBoxTableCell.forTableColumn(i -> model.getColumns().get(i).getPrimaryKey()));

        allowsNull.setCellValueFactory(e -> e.getValue().getNullable());
        allowsNull.setCellFactory(CheckBoxTableCell.forTableColumn(i -> model.getColumns().get(i).getNullable()));

        dataType.setCellValueFactory(e -> e.getValue().getDataType());
        List<String> types = getFieldTypes();
        ObservableList<String> oTypes = FXCollections.observableArrayList(types);
        dataType.setCellFactory(getComboCallback(oTypes));
        dataType.setOnEditCommit(e -> {
            TablePosition<ApricotColumnData, String> pos = e.getTablePosition();
            model.getColumns().get(pos.getRow()).getDataType().setValue(e.getNewValue());
            columnDefinitionTable.getSelectionModel().selectRightCell();
        });

        length.setCellValueFactory(e -> e.getValue().getValueLength());
        length.setCellFactory(TextFieldTableCell.forTableColumn());
        length.setOnEditCommit(e -> {
            TablePosition<ApricotColumnData, String> pos = e.getTablePosition();
            model.getColumns().get(pos.getRow()).getValueLength().setValue(e.getNewValue());
            columnDefinitionTable.getSelectionModel().selectRightCell();
        });

        comment.setCellValueFactory(new PropertyValueFactory<ApricotColumnData, String>("comment"));

        applyModel(model);
    }

    private <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> getComboCallback(final ObservableList<T> items) {
        return new Callback<TableColumn<S, T>, TableCell<S, T>>() {
            @Override
            public TableCell<S, T> call(TableColumn<S, T> list) {
                ComboBoxTableCell<S, T> comboCell = new ComboBoxTableCell<S, T>(items);
                comboCell.comboBoxEditableProperty().setValue(true);

                return comboCell;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private void editFocusedCell() {
        final TablePosition<ApricotColumnData, ?> focusedCell = columnDefinitionTable.focusModelProperty().get()
                .focusedCellProperty().get();
        columnDefinitionTable.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    @FXML
    public void newColumn(ActionEvent event) {
        ApricotColumnData columnData = new ApricotColumnData();
        columnData.getName().setValue("<New Column>");

        model.getColumns().add(columnData);
        columnDefinitionTable.getFocusModel().focus(model.getColumns().size()-1);
    }

    @FXML
    public void upColumn(ActionEvent event) {

    }

    @FXML
    public void downColumn(ActionEvent event) {

    }

    @FXML
    public void deleteColumn(ActionEvent event) {

    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void save(ActionEvent event) {

    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    private void applyModel(EditEntityModel model) {
        entityName.setText(model.getEntityName());
        columnDefinitionTable.itemsProperty().setValue(model.getColumns());
        columnDefinitionTable.refresh();
    }

    private List<String> getFieldTypes() {
        List<String> ret = new ArrayList<>();
        ApricotProject project = projectManager.findCurrentProject();
        ApricotApplicationParameter p = appParameterManager
                .getParameterByName(project.getTargetDatabase() + ".datatypes");
        if (p != null && p.getValue() != null) {
            String[] types = p.getValue().split(";");
            ret = Arrays.asList(types);
        }

        return ret;
    }
}
