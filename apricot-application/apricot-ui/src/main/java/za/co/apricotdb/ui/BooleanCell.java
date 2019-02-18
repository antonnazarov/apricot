package za.co.apricotdb.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class BooleanCell extends TableCell<ReversedTableRow, Boolean> {
    
    private CheckBox checkBox;

    public BooleanCell() {
        checkBox = new CheckBox();
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("--------------" + checkBox.getParent());
                if (checkBox.getParent() != null) {
                    System.out.println("--------------" + checkBox.getParent().getParent());
                    TableRow<ReversedTableRow> tr = (TableRow) checkBox.getParent().getParent();
                    TableView<ReversedTableRow> tv = tr.getTableView();
                    ReversedTableRow r = tv.getSelectionModel().getSelectedItem();
                    System.out.println("--------------table: " + r.getTableName());
                }
                // ReversedTableRow row = tv.getSelectionModel().getSelectedItem();
                // System.out.println("--------------ReversedTableRow.tableName: " + row.getTableName());
                if (isEditing()) {
                    commitEdit(newValue == null ? false : newValue);
                }
            }
        });
        this.setGraphic(checkBox);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(true);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }
        checkBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
    }

    public void commitEdit(Boolean value) {
        super.commitEdit(value);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            checkBox.setSelected(item);
        } else {
            checkBox.setVisible(false);
        }
    }
}
