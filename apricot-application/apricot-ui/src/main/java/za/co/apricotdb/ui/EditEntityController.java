package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

    public void init(EditEntityModel model) {
        columnName.setCellValueFactory(e -> e.getValue().getName());
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        
        primaryKey.setCellValueFactory(e -> e.getValue().getPrimaryKey());
        primaryKey.setCellFactory(CheckBoxTableCell.forTableColumn(i -> model.getColumns().get(i).getPrimaryKey()));
        
        allowsNull.setCellValueFactory(e -> e.getValue().getNullable());
        allowsNull.setCellFactory(CheckBoxTableCell.forTableColumn(i -> model.getColumns().get(i).getNullable()));
        
        dataType.setCellValueFactory(e -> e.getValue().getDataType());
        ObservableList<String> test = FXCollections.observableArrayList("1", "2", "3");
        dataType.setCellFactory(ComboBoxTableCell.forTableColumn(test));
        
        length.setCellValueFactory(e -> e.getValue().getValueLength());
        length.setCellFactory(TextFieldTableCell.forTableColumn());

        applyModel(model);
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
    }
}
