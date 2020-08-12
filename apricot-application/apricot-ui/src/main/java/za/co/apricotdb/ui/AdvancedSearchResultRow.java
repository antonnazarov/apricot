package za.co.apricotdb.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 * The holder of the results of the Advanced Search.
 *
 * @author Anton Nazarov
 * @since 12/08/2020
 */
public class AdvancedSearchResultRow {

    private SimpleStringProperty entityName;
    private CheckBox entitySelected;
    private AdvancedSearchController controller;

    public AdvancedSearchResultRow(String entityName, AdvancedSearchController controller) {
        this.entityName = new SimpleStringProperty(entityName);
        this.controller = controller;
        entitySelected = new CheckBox();
        entitySelected.setOnAction(e -> {
            controller.buildStatusBar();
        });
    }

    public String getEntityName() {
        return entityName.get();
    }

    public SimpleStringProperty entityNameProperty() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName.set(entityName);
    }

    public CheckBox getEntitySelected() {
        return entitySelected;
    }

    public void setEntitySelected(CheckBox entitySelected) {
        this.entitySelected = entitySelected;
    }
}
