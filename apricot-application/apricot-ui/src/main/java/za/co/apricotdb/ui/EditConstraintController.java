package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.model.ApricotColumnData;
import za.co.apricotdb.ui.model.ApricotConstraintData;
import za.co.apricotdb.ui.model.ApricotConstraintValidator;
import za.co.apricotdb.ui.model.EditConstraintModel;
import za.co.apricotdb.ui.model.EditEntityModel;

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

        allColumnsList.itemsProperty().bind(new SimpleListProperty<ApricotColumnData>(model.getAllColumns()));
        selectedColumnsList.itemsProperty().bind(new SimpleListProperty<ApricotColumnData>(model.getSelectedColumns()));

        setCellFactory(allColumnsList);
        setCellFactory(selectedColumnsList);

        //  the fields included into PK or FK have to be not ediable 
        if (!editableFields) {
            allColumnsList.setDisable(true);
            selectedColumnsList.setDisable(true);
            constraintType.setDisable(true);
            selectBtn.setDisable(true);
            deselectBtn.setDisable(true);
            
            constraintType.getItems().addAll(ConstraintType.PRIMARY_KEY.name(), ConstraintType.FOREIGN_KEY.name());
        } else {
            constraintType.getItems().addAll(ConstraintType.UNIQUE_INDEX.name(), ConstraintType.NON_UNIQUE_INDEX.name(),
                    ConstraintType.UNIQUE.name());
            
            allColumnsList.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    selectColumn(null);
                }
            });
            selectedColumnsList.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    deselectColumn(null);
                }
            });
        }
    }

    private void setCellFactory(ListView<ApricotColumnData> list) {
        list.setCellFactory(v -> {
            TextFieldListCell<ApricotColumnData> cell = new TextFieldListCell<ApricotColumnData>();
            cell.setConverter(new StringConverter<ApricotColumnData>() {
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
    public void selectColumn(ActionEvent event) {
        if (allColumnsList.getItems().size() > 0) {
            ApricotColumnData cd = allColumnsList.getSelectionModel().getSelectedItem();
            model.getSelectedColumns().add(cd);
            model.getAllColumns().remove(cd);
        }
    }

    @FXML
    public void deselectColumn(ActionEvent event) {
        if (selectedColumnsList.getItems().size() > 0) {
            ApricotColumnData cd = selectedColumnsList.getSelectionModel().getSelectedItem();
            model.getAllColumns().add(cd);
            model.getSelectedColumns().remove(cd);
        }
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void save(ActionEvent event) {
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
        constraintData.getColumns().clear();
        constraintData.getColumns().addAll(model.getSelectedColumns());

        if (constraintData.isAdded() && !editEntityModel.getConstraints().contains(constraintData)) {
            editEntityModel.getConstraints().add(constraintData);
        }
        constraintsTable.refresh();
        constraintsTable.getSelectionModel().select(constraintData);
        editEntityModel.setEdited(true);
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
