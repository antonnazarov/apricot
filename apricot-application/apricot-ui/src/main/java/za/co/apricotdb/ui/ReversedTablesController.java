package za.co.apricotdb.ui;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.ReverseEngineHandler;

/**
 * The controller of the apricot-re-tables-list.fxml form.
 * 
 * @author Anton Nazarov
 * @since 18/02/2019
 */
@Component
public class ReversedTablesController {

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    ApplicationInitializer applicationInitializer;

    @FXML
    TableView<ReversedTableRow> reversedTablesList;

    @FXML
    TableColumn<ReversedTableRow, String> tableColumn;

    @FXML
    TableColumn<ReversedTableRow, Boolean> includedColumn;

    @FXML
    Label summaryInfo;

    @FXML
    Pane mainPane;

    private MetaData metaData = null;
    private PropertyChangeListener canvasChangeListener;

    public void init(MetaData metaData, String[] blackList, PropertyChangeListener canvasChangeListener) {
        this.metaData = metaData;
        this.canvasChangeListener = canvasChangeListener;

        reversedTablesList.getItems().clear();
        List<ApricotTable> tables = metaData.getTables();
        Collections.sort(tables, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        for (ApricotTable t : metaData.getTables()) {
            ReversedTableRow r = null;
            if (Arrays.stream(blackList).anyMatch(t.getName()::equals)) {
                r = new ReversedTableRow(t, false);
            } else {
                r = new ReversedTableRow(t, true);
            }
            reversedTablesList.getItems().add(r);
        }
        reversedTablesList.getSelectionModel().select(0);

        tableColumn.setCellValueFactory(new PropertyValueFactory<ReversedTableRow, String>("tableName"));
        Callback<TableColumn<ReversedTableRow, Boolean>, TableCell<ReversedTableRow, Boolean>> booleanCellFactory = new Callback<TableColumn<ReversedTableRow, Boolean>, TableCell<ReversedTableRow, Boolean>>() {
            @Override
            public TableCell<ReversedTableRow, Boolean> call(TableColumn<ReversedTableRow, Boolean> p) {
                return new BooleanCell();
            }
        };
        includedColumn.setCellValueFactory(new PropertyValueFactory<ReversedTableRow, Boolean>("included"));
        includedColumn.setCellFactory(booleanCellFactory);
        includedColumn.setStyle("-fx-alignment: CENTER;");

        summaryInfo.setText(metaData.getTables().size() + " tables were scanned; " + blackList.length
                + " tables in the black list");
    }

    @FXML
    public void cancel() {
        getStage().close();
    }

    @FXML
    public void finish() {
        List<ApricotTable> included = new ArrayList<>();
        List<ApricotTable> excluded = new ArrayList<>();
        for (ReversedTableRow r : reversedTablesList.getItems()) {
            if (r.isIncluded()) {
                included.add(r.getTable());
            } else {
                excluded.add(r.getTable());
            }
        }

        if (reverseEngineHandler.saveReversedObjects(included, excluded, metaData.getRelationships())) {
            // refresh the snapshot view
            applicationInitializer.initializeDefault(canvasChangeListener);
            getStage().close();
        }
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
