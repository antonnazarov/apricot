package za.co.apricotdb.ui.model;

import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * This bean is a storage of one line on the Parent Child chain in the Relationship Edit form.
 * 
 * @author Anton Nazarov
 * @since 14/03/2019
 */
public class ParentChildKeyHolder {
    
    private final TextField primaryKeyField;
    private final ComboBox<String> childForeignKey;
    private final CheckBox pk;
    private final CheckBox notNull;
    
    private boolean voidSlot;
    
    public ParentChildKeyHolder(TextField primaryKeyField, ComboBox<String> childForeignKey, CheckBox pk, CheckBox notNull) {
        this.primaryKeyField = primaryKeyField;
        this.childForeignKey = childForeignKey;
        this.pk = pk;
        this.notNull = notNull;
    }
    
    public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
        primaryKeyField.setText(primaryKeyFieldName);
    }
    
    public void populateForeignKey(List<String> tables) {
        childForeignKey.getItems().addAll(tables);
        voidSlot = false;
    }
    
    public void setVoidSlot() {
        primaryKeyField.setVisible(false);
        childForeignKey.setDisable(true);
        childForeignKey.getItems().clear();
        pk.setDisable(true);
        pk.setSelected(false);
        notNull.setDisable(true);
        notNull.setSelected(false);
        
        voidSlot = true;
    }
    
    public void resetSlot() {
        primaryKeyField.setVisible(true);
        childForeignKey.setDisable(false);
        childForeignKey.getItems().clear();
        pk.setDisable(false);
        pk.setSelected(false);
        notNull.setDisable(false);
        notNull.setSelected(false);
        
        voidSlot = true;
    }
    
    public boolean isVoidSlot() {
        return voidSlot;
    }
}
