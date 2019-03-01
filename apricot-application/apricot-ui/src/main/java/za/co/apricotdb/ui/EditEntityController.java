package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import za.co.apricotdb.persistence.data.ApplicationParameterManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.ui.handler.ApricotEntityHandler;
import za.co.apricotdb.ui.model.ApricotColumnData;
import za.co.apricotdb.ui.model.ApricotConstraintData;
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
    
    @Autowired
    ApricotEntityHandler entityHandler;

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
    
    @FXML
    AnchorPane columnsAnchorPane;
    
    @FXML
    TableView<ApricotConstraintData> constraintsTable;
    
    @FXML
    TableColumn<ApricotConstraintData, String> constraintType;

    @FXML
    TableColumn<ApricotConstraintData, String> constraintName;
    
    @FXML
    TableColumn<ApricotConstraintData, String> constraintColumns;

    private EditEntityModel model;

    public void init(EditEntityModel model) {
        this.model = model;

        initColumnsTab();
        initConstraintsTab();

        applyModel(model);
    }
    
    private void initColumnsTab() {
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
    }
    
    private void initConstraintsTab() {
        constraintType.setCellValueFactory(new PropertyValueFactory<ApricotConstraintData, String>("constraintType"));
        constraintName.setCellValueFactory(new PropertyValueFactory<ApricotConstraintData, String>("constraintName"));
        constraintColumns.setCellValueFactory(new PropertyValueFactory<ApricotConstraintData, String>("constraintColumns"));
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
        //  ignore if there is already a new column in process of insert
        if (model.getColumns().get(model.getColumns().size()-1).getName().getValue().equals("<New Column>")) {
            return;
        }
        
        ApricotColumnData columnData = new ApricotColumnData();
        columnData.getName().setValue("<New Column>");
        columnData.setAdded(true);

        model.getColumns().add(columnData);
        focusRow(model.getColumns().size()-1);
    }
    
    private void focusRow(int pos) {
        columnDefinitionTable.getSelectionModel().select(pos, columnName);
        columnDefinitionTable.getFocusModel().focus(pos, columnName);
        columnDefinitionTable.refresh();
    }

    @FXML
    @SuppressWarnings("unchecked")    
    public void upColumn(ActionEvent event) {
        final TablePosition<ApricotColumnData, ?> focusedCell = columnDefinitionTable.focusModelProperty().get()
                .focusedCellProperty().get();        
        int row = focusedCell.getRow();
        if (row == 0) {
            return;
        }
        Collections.swap(model.getColumns(), row, row-1);
        focusRow(row-1);
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void downColumn(ActionEvent event) {
        final TablePosition<ApricotColumnData, ?> focusedCell = columnDefinitionTable.focusModelProperty().get()
                .focusedCellProperty().get();        
        int row = focusedCell.getRow();
        if (row == model.getColumns().size()-1) {
            return;
        }
        Collections.swap(model.getColumns(), row, row+1);
        focusRow(row+1);
    }

    @FXML
    public void deleteColumn(ActionEvent event) {
        if (model.getColumns().size() == 0) {
            return;
        }
        
        ApricotColumnData cd = columnDefinitionTable.getSelectionModel().getSelectedItem();
        if (cd.getColumn() == null || entityHandler.requestColumnDelete(cd.getColumn())) {
            model.getColumns().remove(cd);
            columnDefinitionTable.refresh();
            
            removeRelatedConstraint(cd);
        }
    }
    
    private void removeRelatedConstraint(ApricotColumnData cd) {
        List<ApricotConstraintData> removeConstraints = new ArrayList<>();
        for (ApricotConstraintData acd : model.getConstraints()) {
            for (ApricotColumnData d : acd.getColumns()) {
                if (d.equals(cd)) {
                    removeConstraints.add(acd);
                }
            }
        }
        
        if (removeConstraints.size() > 0) {
            model.getConstraints().removeAll(removeConstraints);
            constraintsTable.refresh();
        }
    }
    
    @FXML
    public void newConstraint(ActionEvent event) {
        
    }
    
    @FXML
    public void editConstraint(ActionEvent event) {
        
    }

    @FXML
    public void deleteConstraint(ActionEvent event) {
        
    }
    
    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void save(ActionEvent event) {
        // TODO implement
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    private void applyModel(EditEntityModel model) {
        entityName.setText(model.getEntityName());
        columnDefinitionTable.itemsProperty().setValue(model.getColumns());
        columnDefinitionTable.refresh();
        
        constraintsTable.itemsProperty().setValue(model.getConstraints());
        constraintsTable.refresh();
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
