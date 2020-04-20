package za.co.apricotdb.ui.comparator;

import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.TreeTableRow;
import javafx.util.Callback;
import za.co.apricotdb.ui.util.UiConstants;

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

                    CompareSnapshotRow cRow = null;
                    TreeTableRow<CompareSnapshotRow> row = getTreeTableRow();

                    if (row != null && row.getTreeItem() != null) {
                        cRow = row.getTreeItem().getValue();
                        row.setTooltip(getToolTip(cRow));
                    }

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else if (item.equals(UiConstants.ELLIPSIS)) {
                        setText(item);
                        setGraphic(null);
                        setStyle("-fx-font-weight: bold;");
                        setTextFill(Color.BLACK);
                    } else if (cRow != null) {
                        setText(item);
                        setStyle(cRow.getState().getSourceStyle(cRow.getType()));
                        setGraphic(cRow.getState().getSourceImage(cRow.getType()));
                        setTextFill(cRow.getState().getSourceColor(cRow.getType()));
                    }
                }
            };
        });
    }

    private Tooltip getToolTip(CompareSnapshotRow row) {
        Tooltip tip = new Tooltip();
        StringBuilder text = new StringBuilder("The ");
        if (row.getType() == CompareRowType.SNAPSHOT) {
            if (row.isDifferent()) {
                text.append("snapshots have been different");
            } else {
                text.append("snapshots are equal");
            }
        } else if (row.getType() == CompareRowType.CONSTRAINT_COLUMNS) {
            if (row.isDifferent()) {
                text.append(
                        "list of the table fields included into the constraint is different in the source and target snapshot");
            } else {
                text.append(
                        "list of the table fields included into the constraint is the same in the source and target snapshot");
            }
        } else {
            if (row.getType() != CompareRowType.RELATIONSHIP) {
                text.append(row.getType().getName()).append(" \"").append(row.getObjectName()).append("\" ");
            } else {
                text.append(row.getType().getName()).append(" ").append(row.getObjectName()).append(" ");
            }
            if (!row.isDifferent()) {
                text.append(UiConstants.OBJECTS_EQUAL);
            } else if (!emptySource(row) && !emptyTarget(row)) {
                text.append(UiConstants.OBJECTS_DIFFERENT);
            } else if (emptySource(row) && !emptyTarget(row)) {
                text.append(UiConstants.OBJECTS_ADDED);
            } else if (!emptySource(row) && emptyTarget(row)) {
                text.append(UiConstants.OBJECTS_REMOVED);
            }
        }

        tip.setText(text.toString());
        Font f = new Font(15);
        tip.setFont(f);

        return tip;
    }

    private boolean emptySource(CompareSnapshotRow row) {
        return empty(row, true);
    }

    private boolean emptyTarget(CompareSnapshotRow row) {
        return empty(row, false);
    }

    private boolean empty(CompareSnapshotRow row, boolean source) {
        if (source) {
            return row.getSource().getValue().equals(UiConstants.ELLIPSIS);
        }
        return row.getTarget().getValue().equals(UiConstants.ELLIPSIS);
    }
}
