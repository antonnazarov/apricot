package za.co.apricotdb.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import za.co.apricotdb.ui.handler.RelatedEntityAbsent;

/**
 * This bean represents the related entity in form/controller:
 * RelatedEntitiesController.
 * 
 * @author Anton Nazarov
 * @since 02/03/2020
 */
public class RelatedEntityRow {

    private SimpleObjectProperty<HBox> entity;
    private CheckBox addToView;
    private RelatedEntityAbsent absentEntity;

    public RelatedEntityRow(RelatedEntityAbsent absentEntity) {
        this.entity = new SimpleObjectProperty<>(modelHBox(absentEntity));
        this.addToView = new CheckBox();
        this.absentEntity = absentEntity;
    }

    public HBox getEntity() {
        return entity.get();
    }

    public CheckBox getAddToView() {
        return addToView;
    }

    public void setAddToView(CheckBox addToView) {
        this.addToView = addToView;
    }
    
    public String getAbsentEntity() {
        return absentEntity.getRelatedTable();
    }

    @Override
    public String toString() {
        return "RelatedEntityRow [entityName=" + absentEntity.getRelatedTable() + ", addToViewFlag="
                + addToView.isSelected() + "]";
    }

    private HBox modelHBox(RelatedEntityAbsent absentEntity) {
        HBox hbox = new HBox();
        if (absentEntity.isChild()) {
            hbox.getChildren().add(getImageView(true));
        } else {
            hbox.getChildren().add(getImageView(false));
        }
        hbox.getChildren().add(new Label(absentEntity.getRelatedTable()));
        if (absentEntity.isParent()) {
            hbox.getChildren().add(getImageView(true));
        } else {
            hbox.getChildren().add(getImageView(false));
        }
        hbox.setSpacing(4);
        
        return hbox;
    }

    private ImageView getImageView(boolean crowFoot) {
        String img = "/za/co/apricotdb/ui/handler/empty-small.png";
        if (crowFoot) {
            img = "/za/co/apricotdb/ui/handler/crow-foot-small.png";
        }
        Image image = new Image(getClass().getResourceAsStream(img));

        return new ImageView(image);
    }
}
