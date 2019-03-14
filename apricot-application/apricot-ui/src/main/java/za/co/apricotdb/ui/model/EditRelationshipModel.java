package za.co.apricotdb.ui.model;

import java.io.Serializable;

import za.co.apricotdb.persistence.entity.ApricotTable;

public class EditRelationshipModel implements Serializable {

    private static final long serialVersionUID = -8071000286285033999L;
    
    private ApricotTable parentTable;
    private ApricotTable childTable;
    
    public EditRelationshipModel(ApricotTable[] selectedTables) {
        this.parentTable = selectedTables[0];
        this.childTable = selectedTables[1];
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
    
}
