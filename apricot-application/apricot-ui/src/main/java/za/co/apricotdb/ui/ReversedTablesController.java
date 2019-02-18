package za.co.apricotdb.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The controller of the apricot-re-tables-list.fxml form.
 * 
 * @author Anton Nazarov
 * @since 18/02/2019
 */
@Component
public class ReversedTablesController {

    @FXML
    TableView<ReversedTableRow> reversedTablesList;

    @FXML
    TableColumn<ReversedTableRow, String> tableColumn;

    @FXML
    TableColumn<ReversedTableRow, Boolean> includedColumn;

    public void init(List<ApricotTable> tables, String[] blackList) {
        reversedTablesList.getItems().clear();
        for (ApricotTable t : tables) {
            ReversedTableRow r = null;
            if (Arrays.stream(blackList).anyMatch(t.getName()::equals)) {
                tableColumn.setStyle("-fx-text-fill:lighgray;");
                r = new ReversedTableRow(t, false);
            } else {
                tableColumn.setStyle("-fx-text-fill:black;");
                r = new ReversedTableRow(t, true);
            }
            reversedTablesList.getItems().add(r);
        }

        tableColumn.setCellValueFactory(new PropertyValueFactory<ReversedTableRow, String>("tableName"));
        Callback<TableColumn<ReversedTableRow, Boolean>, TableCell<ReversedTableRow, Boolean>> booleanCellFactory =         
        new Callback<TableColumn<ReversedTableRow, Boolean>, TableCell<ReversedTableRow, Boolean>>() {
            @Override
            public TableCell<ReversedTableRow, Boolean> call(TableColumn<ReversedTableRow, Boolean> p) {
                return new BooleanCell();
            }
        };
        includedColumn.setCellValueFactory(new PropertyValueFactory<ReversedTableRow, Boolean>("included"));
        includedColumn.setCellFactory(booleanCellFactory);
    }

    @FXML
    public void cancel() {

    }

    @FXML
    public void finish() {

    }
}
