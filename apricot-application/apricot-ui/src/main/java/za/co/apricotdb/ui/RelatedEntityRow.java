package za.co.apricotdb.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 * This bean represents the related entity in form/controller:
 * RelatedEntitiesController.
 * 
 * @author Anton Nazarov
 * @since 02/03/2020
 */
public class RelatedEntityRow {

    private SimpleStringProperty entityName;
    private CheckBox addToView;

    public RelatedEntityRow(String entityName) {
        this.entityName = new SimpleStringProperty(entityName);
        this.addToView = new CheckBox();
    }

    public String getEntityName() {
        return entityName.get();
    }

    public void setEntityName(String entityName) {
        this.entityName.set(entityName);
    }

    public CheckBox getAddToView() {
        return addToView;
    }

    public void setAddToView(CheckBox addToView) {
        this.addToView = addToView;
    }

    @Override
    public String toString() {
        return "RelatedEntityRow [entityName=" + entityName + ", addToViewFlag=" + addToView.isSelected() + "]";
    }
}
