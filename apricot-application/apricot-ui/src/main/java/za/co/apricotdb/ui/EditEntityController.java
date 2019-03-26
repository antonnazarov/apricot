package za.co.apricotdb.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import za.co.apricotdb.persistence.data.ApplicationParameterManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.handler.ApricotConstraintHandler;
import za.co.apricotdb.ui.handler.ApricotEntityHandler;
import za.co.apricotdb.ui.model.ApricotColumnData;
import za.co.apricotdb.ui.model.ApricotConstraintData;
import za.co.apricotdb.ui.model.ApricotConstraintSerializer;
import za.co.apricotdb.ui.model.EditEntityModel;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

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
    ApricotConstraintHandler constraintHandler;

    @Autowired
    AlertMessageDecorator alertDecorator;

    @Autowired
    ApricotConstraintSerializer constraintSerializer;

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

    @FXML
    Button editConstraintButton;

    @FXML
    Button deleteConstraintButton;

    @FXML
    TabPane mainTabPane;

    @FXML
    Tab columnsTab;

    private EditEntityModel model;

    public void init(EditEntityModel model) {
        this.model = model;

        initColumnsTab();
        initConstraintsTab();
        applyModel(model);

        entityName.setOnAction(e -> {
            model.setEdited(true);
        });
    }

    public void selectColumn(int pos, String fieldName) {
        mainTabPane.getSelectionModel().select(columnsTab);
        focusRow(pos, fieldName);
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
            model.setEdited(true);
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
            model.setEdited(true);
        });

        length.setCellValueFactory(e -> e.getValue().getValueLength());
        length.setCellFactory(TextFieldTableCell.forTableColumn());
        length.setOnEditCommit(e -> {
            TablePosition<ApricotColumnData, String> pos = e.getTablePosition();
            model.getColumns().get(pos.getRow()).getValueLength().setValue(e.getNewValue());
            columnDefinitionTable.getSelectionModel().selectRightCell();
            model.setEdited(true);
        });

        comment.setCellValueFactory(new PropertyValueFactory<ApricotColumnData, String>("comment"));
    }

    private void initConstraintsTab() {
        constraintsTable.itemsProperty()
                .bind(new SimpleObjectProperty<ObservableList<ApricotConstraintData>>(model.getConstraints()));
        constraintType
                .setCellValueFactory(new PropertyValueFactory<ApricotConstraintData, String>("constraintTypeAsString"));
        constraintType.setCellFactory(GrayedLabelCell.getCallback());
        constraintName
                .setCellValueFactory(new PropertyValueFactory<ApricotConstraintData, String>("constraintNameAsString"));
        constraintName.setCellFactory(GrayedLabelCell.getCallback());
        constraintColumns
                .setCellValueFactory(new PropertyValueFactory<ApricotConstraintData, String>("constraintColumns"));
        constraintColumns.setCellFactory(GrayedLabelCell.getCallback());

        constraintsTable.setOnMouseClicked(e -> {
            ApricotConstraintData cd = constraintsTable.getSelectionModel().getSelectedItem();
            if (cd.getConstraintType().getValue().equals(ConstraintType.PRIMARY_KEY.name())
                    || cd.getConstraintType().getValue().equals(ConstraintType.FOREIGN_KEY.name())) {
                editConstraintButton.setDisable(true);
                deleteConstraintButton.setDisable(true);
            } else {
                editConstraintButton.setDisable(false);
                deleteConstraintButton.setDisable(false);
                if (e.getClickCount() == 2) {
                    editConstraint(null);
                }
            }
        });
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
        // ignore if there is already a new column in process of insert
        if (model.getColumns().size() > 0
                && model.getColumns().get(model.getColumns().size() - 1).getName().getValue().equals("<New Column>")) {
            return;
        }

        ApricotColumnData columnData = new ApricotColumnData();
        columnData.getName().setValue("<New Column>");
        columnData.setAdded(true);

        model.getColumns().add(columnData);
        focusRow(model.getColumns().size() - 1);
        model.setEdited(true);
    }

    private void focusRow(int pos) {
        focusRow(pos, "columnName");
    }

    private void focusRow(int pos, String fieldName) {
        TableColumn<ApricotColumnData, String> column = null;
        if (fieldName.equals("columnName")) {
            column = columnName;
        } else {
            column = dataType;
        }
        columnDefinitionTable.getSelectionModel().select(pos, column);
        columnDefinitionTable.getFocusModel().focus(pos, column);
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
        Collections.swap(model.getColumns(), row, row - 1);
        focusRow(row - 1);
        model.setEdited(true);
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void downColumn(ActionEvent event) {
        final TablePosition<ApricotColumnData, ?> focusedCell = columnDefinitionTable.focusModelProperty().get()
                .focusedCellProperty().get();
        int row = focusedCell.getRow();
        if (row == model.getColumns().size() - 1) {
            return;
        }
        Collections.swap(model.getColumns(), row, row + 1);
        focusRow(row + 1);
        model.setEdited(true);
    }

    @FXML
    public void deleteColumn(ActionEvent event) {
        if (model.getColumns().size() == 0) {
            return;
        }

        ApricotColumnData cd = columnDefinitionTable.getSelectionModel().getSelectedItem();
        if (requestColumnDelete(cd)) {
            model.getDeletedColumns().add(cd);
            model.getColumns().remove(cd);
            columnDefinitionTable.refresh();

            removeRelatedConstraint(cd);
            model.setEdited(true);
        }
    }

    /**
     * Get constraints to be deleted together with the target column.
     */
    private List<ApricotConstraintData> getRemovalConstraints(ApricotColumnData cd) {
        List<ApricotConstraintData> removeConstraints = new ArrayList<>();
        for (ApricotConstraintData acd : model.getConstraints()) {
            for (ApricotColumnData d : acd.getColumns()) {
                if (d.equals(cd)) {
                    removeConstraints.add(acd);
                }
            }
        }

        return removeConstraints;
    }

    public boolean requestColumnDelete(ApricotColumnData cd) {
        List<ApricotConstraintData> removeConstraints = getRemovalConstraints(cd);

        if (removeConstraints.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (ApricotConstraintData c : removeConstraints) {
                sb.append(c.getConstraintTypeAsString()).append(" (").append(c.getConstraintNameAsString())
                        .append(")\n");
            }

            ButtonType yes = new ButtonType("Delete", ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(AlertType.WARNING, null, yes, no);
            alert.setTitle("Delete Column");
            alert.setHeaderText("The following constraints, attached to the column \"" + cd.getName().getValue()
                    + "\" will be deleted:\n" + sb.toString());
            alertDecorator.decorateAlert(alert);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Remove the related constraints from the model.
     */
    private void removeRelatedConstraint(ApricotColumnData cd) {
        List<ApricotConstraintData> removeConstraints = getRemovalConstraints(cd);

        if (removeConstraints.size() > 0) {
            model.getDeletedConstraints().addAll(removeConstraints);
            model.getConstraints().removeAll(removeConstraints);
            constraintsTable.refresh();
        }
    }

    @FXML
    public void newConstraint(ActionEvent event) {
        try {
            constraintHandler.openConstraintEditorForm(true, null, model, constraintsTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editConstraint(ActionEvent event) {
        ApricotConstraintData cd = constraintsTable.getSelectionModel().getSelectedItem();
        if (cd.getConstraintType().getValue().equals(ConstraintType.PRIMARY_KEY.name())
                || cd.getConstraintType().getValue().equals(ConstraintType.FOREIGN_KEY.name())) {
            return;
        }
        try {
            constraintHandler.openConstraintEditorForm(false, cd, model, constraintsTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteConstraint(ActionEvent event) {
        if (model.getConstraints().size() == 0) {
            return;
        }

        ApricotConstraintData cd = constraintsTable.getSelectionModel().getSelectedItem();
        if (cd.getConstraintType().getValue().equals(ConstraintType.PRIMARY_KEY.name())
                || cd.getConstraintType().getValue().equals(ConstraintType.FOREIGN_KEY.name())) {
            return;
        }
        model.getDeletedConstraints().add(cd);
        model.getConstraints().remove(cd);
        constraintsTable.refresh();
    }

    @FXML
    public void cancel(ActionEvent event) {
        if (model.isEdited() || model.isNewEntity()) {
            if (alertDecorator.requestYesNoOption("Exit", "You are about to loose the changes you've made", "Exit")) {
                getStage().close();
            }
        } else {
            getStage().close();
        }
    }

    @FXML
    public void save(ActionEvent event) {
        if (entityHandler.saveEntity(model, entityName.getText(), this)) {
            getStage().close();
        }
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

    public enum CurrentTab {
        COLUMNS, CONSTRAINTS;
    }

    public CurrentTab getCurrentTab() {
        if (mainTabPane.getSelectionModel().getSelectedItem() == columnsTab) {
            return CurrentTab.COLUMNS;
        }

        return CurrentTab.CONSTRAINTS;
    }

    public boolean isLastColumn() {
        int idx = columnDefinitionTable.getSelectionModel().getSelectedIndex();
        if (model.getColumns().size() - 1 == idx) {
            return true;
        }
        return false;
    }
}
