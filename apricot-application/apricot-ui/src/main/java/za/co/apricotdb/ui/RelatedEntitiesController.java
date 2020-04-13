package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.ApricotViewHandler;
import za.co.apricotdb.ui.handler.RelatedEntityAbsent;

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
    TableColumn<RelatedEntityRow, HBox> relatedEntity;

    @FXML
    TableColumn<RelatedEntityRow, CheckBox> addColumn;

    /**
     * Initialize this controller.
     */
    public void init(List<RelatedEntityAbsent> relatedEntities) {
        relatedEntity.setCellValueFactory(new PropertyValueFactory<RelatedEntityRow, HBox>("entity"));
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
                addEntities.add(r.getAbsentEntity());
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
    private ObservableList<RelatedEntityRow> getRows(List<RelatedEntityAbsent> entities) {
        // sort the absent entities alphabetically
        Collections.sort(entities, new Comparator<RelatedEntityAbsent>() {
            @Override
            public int compare(RelatedEntityAbsent o1, RelatedEntityAbsent o2) {
                return o1.getRelatedTable().compareTo(o2.getRelatedTable());
            }
        });

        List<RelatedEntityRow> rows = new ArrayList<>();
        entities.forEach(absentEntity -> {
            rows.add(new RelatedEntityRow(absentEntity));
        });

        return FXCollections.observableArrayList(rows);
    }
}
