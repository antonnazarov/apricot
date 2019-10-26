package za.co.apricotdb.ui.comparator;

import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
                    super.updateItem(item, empty);

                    TreeTableRow<CompareSnapshotRow> row = getTreeTableRow();
                    String eq = "none";
                    if (row.getTreeItem() != null) {
                        CompareSnapshotRow cRow = row.getTreeItem().getValue();
                        eq = cRow.getDiff().getValue().toString();
                    }

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString() + " --> " + eq);
                        this.setStyle("-fx-font-weight: bold;");
                        ImageView img = new ImageView();
                        img.setImage(new Image(getClass()
                                .getResourceAsStream("/za/co/apricotdb/ui/comparator/relationship-not-equal.png")));
                        this.setGraphic(img);
                    }
                }
            };
        });
    }
}
