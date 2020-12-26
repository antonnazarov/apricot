package za.co.apricotdb.ui.model;

import javafx.beans.property.SimpleStringProperty;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditRelationshipModel implements Serializable {

    private static final long serialVersionUID = -8071000286285033999L;

    private SimpleStringProperty relationshipName = new SimpleStringProperty();
    private ApricotTable parentTable;
    private ApricotTable childTable;
    private ApricotConstraint constraint;
    private ApricotRelationship relationship;
    private Map<String, ParentChildKeyHolder> keys = new HashMap<>();

    public EditRelationshipModel(ApricotTable[] selectedTables) {
        this.parentTable = selectedTables[0];
        this.childTable = selectedTables[1];
    }

    public EditRelationshipModel(ApricotRelationship relationship) {
        relationshipName.setValue(relationship.getChild().getName());
        parentTable = relationship.getParent().getTable();
        childTable = relationship.getChild().getTable();
        constraint = relationship.getChild();
        this.relationship = relationship;
    }
    
    public void setParentTable(ApricotTable parentTable) {
        this.parentTable = parentTable;
    }
    
    public void setChildTable(ApricotTable childTable) {
        this.childTable = childTable;
    }

    public ApricotTable getParentTable() {
        return parentTable;
    }

    public ApricotTable getChildTable() {
        return childTable;
    }

    public boolean isAutoRelationship() {
        return parentTable.equals(childTable);
    }

    public void swapTables() {
        ApricotTable tmp = parentTable;
        parentTable = childTable;
        childTable = tmp;
    }

    public void addKey(ParentChildKeyHolder key) {
        keys.put(key.getChildForeignKeyComboId(), key);
    }

    public void resetKeys() {
        for (ParentChildKeyHolder h : keys.values()) {
            h.resetSlot();
        }
    }

    public SimpleStringProperty getRelationshipNameProperty() {
        return relationshipName;
    }

    public void populateChildKeyDropDown(String key, List<String> fields) {
        ParentChildKeyHolder keyHolder = keys.get(key);
        if (keyHolder != null) {
            keyHolder.populateForeignKey(fields);
        }
    }

    public void setChildConstraintField(String key, ApricotColumnConstraint col) {
        ParentChildKeyHolder keyHolder = keys.get(key);
        if (keyHolder != null) {
            keyHolder.setForeignField(col.getColumn().getName());
        }
    }
    
    public void setPimaryKeyField(String key, String field) {
        ParentChildKeyHolder keyHolder = keys.get(key);
        if (keyHolder != null) {
            keyHolder.setPrimaryKeyFieldName(field);
        }
    }
    
    public void setVoidSlot(String key) {
        ParentChildKeyHolder keyHolder = keys.get(key);
        if (keyHolder != null) {
            keyHolder.setVoidSlot();
        }
    }
    
    public void setPrimaryKeyFlag(String key, boolean pkFlag) {
        ParentChildKeyHolder keyHolder = keys.get(key);
        if (keyHolder != null) {
            keyHolder.setPrimaryKeyFlag(pkFlag);
        }
    }
    
    public void initColumnAttributes(String key, boolean isPk, boolean isNotNull) {
        ParentChildKeyHolder keyHolder = keys.get(key);
        if (keyHolder != null) {
            keyHolder.initColumnAttributes(isPk, isNotNull);
        }
    }
    
    public void resetColumnAttributes(String key) {
        ParentChildKeyHolder keyHolder = keys.get(key);
        if (keyHolder != null) {
            keyHolder.resetColumnAttributes();
        }
    }
    
    /**
     * Return a collection of the filled foreign keys only. 
     */
    public List<ParentChildKeyHolder> getKeys() {
        List<ParentChildKeyHolder> ret = new ArrayList<>();
        
        for (ParentChildKeyHolder h : keys.values()) {
            if (!h.isVoidSlot()) {
                ret.add(h);
            }
        }
        
        return ret;
    }
    
    public ParentChildKeyHolder getHolderByFkName(String fkName) {
        for (ParentChildKeyHolder h : keys.values()) {
            if (h.getForeignKeyField().equals(fkName)) {
                return h;
            }
        }
        
        return null;
    }
    
    public boolean isPkConstraint() {
        for (ParentChildKeyHolder h : keys.values()) {
            if (h.isPrimaryKey()) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean hasNewFields() {
        for (ParentChildKeyHolder h : keys.values()) {
            if (childTable.getColumnByName(h.getForeignKeyField()) == null) {
                return true;
            }
        }
        
        return false;
    }

    public ApricotConstraint getConstraint() {
        return constraint;
    }

    public ApricotRelationship getRelationship() {
        return relationship;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("EditRelationshipModel: ")
            .append("relationshipName=[").append(relationshipName.getValue())
            .append("], parentTable=[").append(parentTable.getName())
            .append("], childTable=[").append(childTable.getName()).append("\n")
            .append("], keys=[").append(keys);
        
        return sb.toString();
    }
}
