package za.co.apricotdb.ui;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    private EditConstraintModel model;
    private TableView<ApricotConstraintData> constraintsTable;
    private EditEntityModel editEntityModel;

    public void init(EditConstraintModel model, TableView<ApricotConstraintData> constraintsTable,
            EditEntityModel editEntityModel) {
        this.model = model;
        this.constraintsTable = constraintsTable;
        this.editEntityModel = editEntityModel;

        constraintName.setText(model.getConstraintName().getValue());
        constraintType.setValue(model.getConstraintType().getValue());
        allColumnsList.itemsProperty().bind(new SimpleListProperty<ApricotColumnData>(model.getAllColumns()));
        selectedColumnsList.itemsProperty().bind(new SimpleListProperty<ApricotColumnData>(model.getSelectedColumns()));

        setCellFactory(allColumnsList);
        setCellFactory(selectedColumnsList);

        constraintType.getItems().addAll(ConstraintType.UNIQUE_INDEX.name(), ConstraintType.NON_UNIQUE_INDEX.name(),
                ConstraintType.UNIQUE.name());
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
        ApricotConstraintData constraintData = model.getConstraintData();
        if (model.isNewConstraint()) {
            editEntityModel.getConstraints().add(constraintData);
            constraintData.setAdded(true);
        }
        
        // I assume, that something was edited is the existing constraint was saved
        // in the editor form
        constraintData.setEdited(true);

        constraintData.getConstraintName().setValue(constraintName.getText());
        constraintData.getConstraintType().setValue(constraintType.getValue());
        constraintData.getColumns().clear();
        constraintData.getColumns().addAll(model.getSelectedColumns());

        constraintsTable.refresh();
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
