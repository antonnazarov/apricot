package za.co.apricotdb.ui;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import za.co.apricotdb.persistence.entity.ConstraintType;
import za.co.apricotdb.ui.model.ApricotConstraintData;

public class GrayedLabelCell extends TableCell<ApricotConstraintData, String> {

    private Label label = null;

    public GrayedLabelCell() {
        label = new Label();

        this.setGraphic(label);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(false);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (this.getTableView().getItems().size() - 1 >= this.getTableRow().getIndex() && this.getTableRow().getIndex() >= 0) {
            label.setText(item);
            ApricotConstraintData constraint = this.getTableView().getItems().get(this.getTableRow().getIndex());
            if (constraint.getConstraintType().getValue().equals(ConstraintType.PRIMARY_KEY.name())
                    || constraint.getConstraintType().getValue().equals(ConstraintType.FOREIGN_KEY.name())) {

                label.setStyle("-fx-text-fill: blue;");
            }
        }
    }

    public static Callback<TableColumn<ApricotConstraintData, String>, TableCell<ApricotConstraintData, String>> getCallback() {
        Callback<TableColumn<ApricotConstraintData, String>, TableCell<ApricotConstraintData, String>> cellFactory = new Callback<TableColumn<ApricotConstraintData, String>, TableCell<ApricotConstraintData, String>>() {
            @Override
            public TableCell<ApricotConstraintData, String> call(TableColumn<ApricotConstraintData, String> p) {
                return new GrayedLabelCell();
            }
        };

        return cellFactory;
    }
}
