package za.co.apricotdb.support.wrappers;

import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * This wrapper simplifies the text representation of the original ApricotTable
 * entity.
 *
 * @author Anton Nazarov
 * @since 24/10/2018
 */
public class ApricotTextTable implements ApricotTableWrapper {

    private ApricotTable table;
    private List<ApricotTextColumn> columns;

    @Override
    public ApricotColumnWrapper getColumnByName(String columnName) {
        for (ApricotTextColumn c : columns) {
            if (c.getColumnName().equals(columnName)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String getTableName() {
        return table.getName();
    }

    @Override
    public List<String> getChildTableNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ApricotConstraintWrapper> getConstraints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
