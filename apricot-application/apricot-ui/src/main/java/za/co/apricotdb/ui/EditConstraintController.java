package za.co.apricotdb.ui;

import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.model.ApricotColumnData;
import za.co.apricotdb.ui.model.ApricotConstraintData;
import za.co.apricotdb.ui.model.ApricotConstraintValidator;
import za.co.apricotdb.ui.model.EditConstraintModel;
import za.co.apricotdb.ui.model.EditEntityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller is for the apricot-constraint-editor.fxml form.
 *
 * @author Anton Nazarov
 * @since 01/03/2019
 */
@Component
public class EditConstraintController {

    @Autowired
    ApricotConstraintValidator constraintValidator;

    @FXML
    Pane mainPane;

    @FXML
    TextField constraintName;

    @FXML
    ChoiceBox<String> constraintType;

    @FXML
    ListView<ApricotColumnData> allColumnsList;

    @FXML
    ListView<ApricotColumnData> selectedColumnsList;

    @FXML
    Button selectBtn;

    @FXML
    Button deselectBtn;

    @FXML
    Button upBtn;

    @FXML
    Button downBtn;

    private EditConstraintModel model;
    private TableView<ApricotConstraintData> constraintsTable;
    private EditEntityModel editEntityModel;

    public void init(EditConstraintModel model, TableView<ApricotConstraintData> constraintsTable,
                     EditEntityModel editEntityModel, boolean editableFields) {
        this.model = model;
        this.constraintsTable = constraintsTable;
        this.editEntityModel = editEntityModel;

        constraintName.setText(model.getConstraintName().getValue());
        model.getConstraintName().bind(constraintName.textProperty());
        constraintType.setValue(model.getConstraintType().getValue());
        model.getConstraintType().bind(constraintType.valueProperty());

        allColumnsList.itemsProperty().bind(new SimpleListProperty<>(model.getAllColumns()));
        selectedColumnsList.itemsProperty().bind(new SimpleListProperty<>(model.getSelectedColumns()));

        setCellFactory(allColumnsList);
        setCellFactory(selectedColumnsList);

        //  the fields included into PK or FK have to be not editable
        if (!editableFields) {
            allColumnsList.setDisable(true);
            constraintType.setDisable(true);
            selectBtn.setDisable(true);
            deselectBtn.setDisable(true);

            constraintType.getItems().addAll(ConstraintType.PRIMARY_KEY.name(), ConstraintType.FOREIGN_KEY.name());
        } else {
            constraintType.getItems().addAll(ConstraintType.UNIQUE_INDEX.name(), ConstraintType.NON_UNIQUE_INDEX.name(),
                    ConstraintType.UNIQUE.name());

            allColumnsList.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    selectColumn();
                }
            });
            selectedColumnsList.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    deselectColumn();
                }
            });
        }

        selectedColumnsList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                ApricotColumnData acd = selectedColumnsList.getSelectionModel().getSelectedItem();
                if (acd != null) {
                    manageUpDownButtons();
                }
            }
        });

        selectedColumnsList.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                manageUpDownButtons();
            }
        });

        manageLeftRightButtons();
        manageUpDownButtons();
    }

    private void setCellFactory(ListView<ApricotColumnData> list) {
        list.setCellFactory(v -> {
            TextFieldListCell<ApricotColumnData> cell = new TextFieldListCell<>();
            cell.setConverter(new StringConverter<>() {
                @Override
                public String toString(ApricotColumnData column) {
                    return column.getName().getValue();
                }

                @Override
                public ApricotColumnData fromString(String value) {
                    ApricotColumnData column = cell.getItem();
                    column.getName().setValue(value);
                    return column;
                }
            });
            return cell;
        });
    }

    @FXML
    public void selectColumn() {
        if (allColumnsList.getItems().size() > 0) {
            ApricotColumnData cd = allColumnsList.getSelectionModel().getSelectedItem();
            model.getSelectedColumns().add(cd);
            model.getAllColumns().remove(cd);

            manageLeftRightButtons();
        }
    }

    @FXML
    public void deselectColumn() {
        if (selectedColumnsList.getItems().size() > 0) {
            ApricotColumnData cd = selectedColumnsList.getSelectionModel().getSelectedItem();
            model.getAllColumns().add(cd);
            model.getSelectedColumns().remove(cd);

            manageLeftRightButtons();
        }
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    @FXML
    public void save() {
        if (!constraintValidator.validate(model, editEntityModel)) {
            return;
        }

        ApricotConstraintData constraintData = model.getConstraintData();
        if (model.isNewConstraint()) {
            constraintData.setAdded(true);
        }
        // I assume, that something was edited is the existing constraint was saved
        // in the editor form
        constraintData.setEdited(true);
        constraintData.getConstraintName().setValue(constraintName.getText());
        constraintData.getConstraintType().setValue(constraintType.getValue());

        if (constraintData.isAdded() && !editEntityModel.getConstraints().contains(constraintData)) {
            editEntityModel.getConstraints().add(constraintData);
        }
        constraintsTable.refresh();
        constraintsTable.getSelectionModel().select(constraintData);
        editEntityModel.setEdited(true);

        List<ApricotColumnData> columnSelectionCopy = new ArrayList(selectedColumnsList.getItems());
        model.getSelectedColumns().clear();
        model.getSelectedColumns().addAll(columnSelectionCopy);

        constraintData.getColumns().clear();
        constraintData.getColumns().addAll(columnSelectionCopy);

        getStage().close();
    }

    @FXML
    public void upColumn() {
        moveColumn(true);
    }

    @FXML
    public void downColumn() {
        moveColumn(false);
    }

    private void moveColumn(boolean up) {
        int idx = selectedColumnsList.getSelectionModel().getSelectedIndex();
        int size = selectedColumnsList.getItems().size();

        if (size > 0 && idx >= 0) {
            List<ApricotColumnData> items = selectedColumnsList.getItems();
            ApricotColumnData item = items.remove(idx);
            if (up) {
                idx--;
                if (idx < 0) {
                    idx = 0;
                }
            } else {
                idx++;
                if (idx >= size) {
                    idx = 0;
                }
            }
            items.add(idx, item);
            selectedColumnsList.getSelectionModel().select(idx);
            manageUpDownButtons();
        }
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    private void manageLeftRightButtons() {
        if (allColumnsList.isEditable()) {
            if (allColumnsList.getItems().isEmpty()) {
                selectBtn.setDisable(true);
            } else {
                selectBtn.setDisable(false);
            }

            if (selectedColumnsList.getItems().isEmpty()) {
                deselectBtn.setDisable(true);
            } else {
                deselectBtn.setDisable(false);
            }
        }
    }

    private void manageUpDownButtons() {
        int idx = selectedColumnsList.getSelectionModel().getSelectedIndex();
        int size = selectedColumnsList.getItems().size();
        if (idx >= 0 && size > 1) {
            if (idx == 0) {
                upBtn.setDisable(true);
                downBtn.setDisable(false);
            } else if (idx == size - 1) {
                upBtn.setDisable(false);
                downBtn.setDisable(true);
            } else {
                upBtn.setDisable(false);
                downBtn.setDisable(false);
            }
        } else {
            upBtn.setDisable(true);
            downBtn.setDisable(true);
        }
    }
}
