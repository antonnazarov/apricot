package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.ApricotViewHandler;

/**
 * This controller is a background of the form: apricot-related-entities.fxml
 * 
 * @author Anton Nazarov
 * @since 02/03/2020
 */
@Component
public class RelatedEntitiesController {
    
    @Autowired
    ApricotViewHandler viewHandler;

    @FXML
    Pane mainPane;

    @FXML
    TableView<RelatedEntityRow> reversedTables;

    @FXML
    TableColumn<RelatedEntityRow, String> relatedEntity;

    @FXML
    TableColumn<RelatedEntityRow, CheckBox> addColumn;

    /**
     * Initialize this controller.
     */
    public void init(List<String> relatedEntities) {
        relatedEntity.setCellValueFactory(new PropertyValueFactory<RelatedEntityRow, String>("entityName"));
        addColumn.setCellValueFactory(new PropertyValueFactory<RelatedEntityRow, CheckBox>("addToView"));
        addColumn.setStyle("-fx-alignment: CENTER;");

        reversedTables.setItems(getRows(relatedEntities));
    }

    @FXML
    public void selectAll() {
        reversedTables.getItems().forEach(e -> {
            e.getAddToView().setSelected(true);
        });
    }

    @FXML
    public void unselectAll() {
        reversedTables.getItems().forEach(e -> {
            e.getAddToView().setSelected(false);
        });
    }

    @FXML
    public void addToView() {
        List<String> addEntities = new ArrayList<>();
        for (RelatedEntityRow r : reversedTables.getItems()) {
            if (r.getAddToView().isSelected()) {
                addEntities.add(r.getEntityName());
            }
        }
        viewHandler.addEntityToView(addEntities);
        getStage().close();
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    /**
     * Prepare the observable list of values.
     */
    private ObservableList<RelatedEntityRow> getRows(List<String> entities) {
        Collections.sort(entities);
        List<RelatedEntityRow> rows = new ArrayList<>();
        entities.forEach(entityName -> {
            rows.add(new RelatedEntityRow(entityName));
        });

        return FXCollections.observableArrayList(rows);
    }
}
