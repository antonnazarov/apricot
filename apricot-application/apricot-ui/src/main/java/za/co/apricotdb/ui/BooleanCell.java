package za.co.apricotdb.ui;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
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
                if (checkBox.getParent() != null) {
                    Parent p1 = checkBox.getParent();
                    if (p1.getParent() != null) {
                        TableRow<ReversedTableRow> tr = (TableRow) p1.getParent();
                        List<Node> cols = tr.getChildrenUnmodifiable();
                        Node tc = cols.get(0);
                        String s = tc.toString();
                        String tableName = s.substring(s.indexOf("'")+1, s.length()-1);
                        TableView<ReversedTableRow> tv = tr.getTableView();
                        for (ReversedTableRow rtr : tv.getItems()) {
                            if (rtr.getTableName().equals(tableName)) {
                                rtr.setIncluded(newValue);
                                tv.getSelectionModel().select(rtr);
                                break;
                            }
                        }
                    }
                }
                
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
