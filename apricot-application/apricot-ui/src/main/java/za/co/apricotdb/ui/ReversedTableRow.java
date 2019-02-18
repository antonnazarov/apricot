package za.co.apricotdb.ui;

import za.co.apricotdb.persistence.entity.ApricotTable;

public class ReversedTableRow {
    private ApricotTable table;
    private boolean included;

    public ReversedTableRow(ApricotTable table, boolean includedFlag) {
        this.table = table;
        this.included = includedFlag;
    }

    public ApricotTable getTable() {
        return table;
    }
    
    public String getTableName() {
        return table.getName();
    }

    public boolean isIncluded() {
        return included;
    }
}
