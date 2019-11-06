package za.co.apricotdb.ui.comparator;

import org.springframework.stereotype.Component;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        column.setText(null);
        column.setGraphic(getImage("header-diff.png"));

        column.setCellValueFactory(e -> e.getValue().getValue().getEqualize());

        column.setCellFactory(clmn -> {
            return new CheckBoxTreeTableCell<CompareSnapshotRow, Boolean>() {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    setAlignment(Pos.CENTER);

                    CompareSnapshotRow cRow = null;
                    final TreeTableRow<CompareSnapshotRow> row = getTreeTableRow();
                    if (row.getTreeItem() != null) {
                        cRow = row.getTreeItem().getValue();
                    }

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (cRow != null && !cRow.isDifferent()) {
                            setGraphic(getImage("equal-all.png"));
                        } else if (cRow != null) {
                            CheckBox cb = (CheckBox) getGraphic();
                            cb.disableProperty().unbind();
                            cb.disableProperty().bind(cRow.getCheckBoxDisabled());
                            cb.setOnAction(e -> {
                                if (cb.isSelected()) {
                                    modifyChildItemsRecursively(row.getTreeItem(), true);
                                } else {
                                    modifyChildItemsRecursively(row.getTreeItem(), false);
                                }
                            });
                        }
                    }
                }
            };
        });
    }

    private void modifyChildItemsRecursively(TreeItem<CompareSnapshotRow> item, boolean disable) {
        if (item.getChildren() != null && !item.getChildren().isEmpty()) {
            for (TreeItem<CompareSnapshotRow> itm : item.getChildren()) {
                itm.getValue().getCheckBoxDisabled().setValue(disable);
                modifyChildItemsRecursively(itm, disable);
            }
        }
    }

    private ImageView getImage(String image) {
        ImageView img = new ImageView();
        img.setImage(new Image(getClass().getResourceAsStream(CompareState.ICON_PATH + image)));

        return img;
    }
}
