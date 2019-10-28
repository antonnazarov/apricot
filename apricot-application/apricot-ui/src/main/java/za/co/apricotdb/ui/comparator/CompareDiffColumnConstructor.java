package za.co.apricotdb.ui.comparator;

import org.springframework.stereotype.Component;

import javafx.geometry.Pos;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.cell.CheckBoxTreeTableCell;

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
        column.setCellValueFactory(e -> e.getValue().getValue().getEqualize());

        column.setCellFactory(clmn -> {
            return new CheckBoxTreeTableCell<CompareSnapshotRow, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    setAlignment(Pos.CENTER);

                    CompareSnapshotRow cRow = null;
                    TreeTableRow<CompareSnapshotRow> row = getTreeTableRow();
                    if (row.getTreeItem() != null) {
                        cRow = row.getTreeItem().getValue();
                    }

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (cRow != null && !cRow.getDiff()) {
                            setGraphic(cRow.getState().getSourceImage(cRow.getType()));
                        }
                    }
                }
            };
        });
    }
}
