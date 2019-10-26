package za.co.apricotdb.ui.comparator;

import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * The constructor component for the middle (difference) column of the Snapshot
 * comparator table.
 * 
 * @author Anton Nazarov
 * @since 26/10/2019
 */
@Component
public class CompareDiffColumnConstructor implements CompareColumnConstructor<CompareSnapshotRow, Boolean> {

    @Override
    public void construct(TreeTableColumn<CompareSnapshotRow, Boolean> column) {
        column.setCellValueFactory(
                new Callback<TreeTableColumn.CellDataFeatures<CompareSnapshotRow, Boolean>, ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(CellDataFeatures<CompareSnapshotRow, Boolean> param) {
                        TreeItem<CompareSnapshotRow> item = param.getValue();
                        return item.getValue().getDiff();
                    }
                });
    }
}
