package za.co.apricotdb.ui.comparator;

import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.util.Callback;

/**
 * The constructor component for the source column of the Snapshot comparator
 * table.
 * 
 * @author Anton Nazarov
 * @since 26/10/2019
 */
@Component
public class CompareSourceColumnConstructor implements CompareColumnConstructor<CompareSnapshotRow, String> {

    @Override
    public void construct(TreeTableColumn<CompareSnapshotRow, String> column) {
        column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<CompareSnapshotRow, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(CellDataFeatures<CompareSnapshotRow, String> param) {
                        TreeItem<CompareSnapshotRow> item = param.getValue();
                        return item.getValue().getSource();
                    }
                });

        column.setCellFactory(clmn -> {
            return new TreeTableCell<CompareSnapshotRow, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    updateItem(item, empty);

                    CompareSnapshotRow cRow = null;
                    TreeTableRow<CompareSnapshotRow> row = getTreeTableRow();
                    if (row.getTreeItem() != null) {
                        cRow = row.getTreeItem().getValue();
                    }

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        setStyle(cRow.getState().getSourceStyle(cRow.getType()));
                        setGraphic(cRow.getState().getSourceImage(cRow.getType()));
                        setTextFill(cRow.getState().getSourceColor(cRow.getType()));
                    }
                }
            };
        });
    }
}
