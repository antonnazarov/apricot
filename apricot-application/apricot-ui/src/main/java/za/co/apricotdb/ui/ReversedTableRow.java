package za.co.apricotdb.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.ui.util.ImageHelper;
import za.co.apricotdb.viewport.canvas.ElementType;

public class ReversedTableRow {

    private static final String TABLE_BLACK = "/za/co/apricotdb/ui/handler/table-small-black.jpg";
    private static final String TABLE_GREY = "/za/co/apricotdb/ui/handler/table-small-gray.jpg";
    private static final String VIEW_BLACK = "/za/co/apricotdb/ui/handler/database-view-18-black.png";
    private static final String VIEW_GREY = "/za/co/apricotdb/ui/handler/database-view-18-grey.png";

    private SimpleObjectProperty<HBox> element;
    private ApricotTable table;
    private HBox included;
    private ElementType elementType;

    public ReversedTableRow(ApricotTable table, boolean includedFlag, ElementType elementType) {
        this.table = table;
        included = new HBox();
        included.setStyle("-fx-alignment: CENTER;");
        CheckBox cb = new CheckBox();
        cb.setSelected(includedFlag);
        included.getChildren().add(cb);
        this.elementType = elementType;
        this.element = new SimpleObjectProperty<>(modelHBox(elementType, includedFlag));

        cb.setOnAction(e -> {
            HBox elm = element.get();
            elm.getChildren().clear();
            elm.getChildren().addAll(modelHBox(elementType, getCheckBox().isSelected()).getChildren());
        });
    }

    public ApricotTable getTable() {
        return table;
    }
    
    public String getTableName() {
        return table.getName();
    }

    public HBox getIncluded() {
        return included;
    }

    public boolean isIncluded() {
        return getCheckBox().isSelected();
    }
    
    public void setIncluded(boolean included) {
        getCheckBox().setSelected(included);

        HBox elm = element.get();
        elm.getChildren().clear();
        elm.getChildren().addAll(modelHBox(elementType, getCheckBox().isSelected()).getChildren());
    }

    private CheckBox getCheckBox() {
        if (included.getChildren() != null && !included.getChildren().isEmpty() && included.getChildren().get(0) instanceof CheckBox) {
            return (CheckBox) included.getChildren().get(0);
        }

        return null;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public HBox getElement() {
        return element.get();
    }

    private HBox modelHBox(ElementType elementType, boolean included) {
        HBox hbox = new HBox();
        if (elementType == ElementType.ENTITY) {
            if (included) {
                hbox.getChildren().add(new ImageView(ImageHelper.getImage(TABLE_BLACK, getClass())));
            } else {
                hbox.getChildren().add(new ImageView(ImageHelper.getImage(TABLE_GREY, getClass())));
            }
        } else {
            if (included) {
                hbox.getChildren().add(new ImageView(ImageHelper.getImage(VIEW_BLACK, getClass())));
            } else {
                hbox.getChildren().add(new ImageView(ImageHelper.getImage(VIEW_GREY, getClass())));
            }
        }

        Label label = new Label(getTableName());
        if (included) {
            label.setTextFill(Color.BLACK);
        } else {
            label.setTextFill(Color.GREY);
        }
        hbox.getChildren().add(label);

        hbox.setSpacing(4);

        return hbox;
    }
}
