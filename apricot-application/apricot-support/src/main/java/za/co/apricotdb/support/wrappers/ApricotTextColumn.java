package za.co.apricotdb.support.wrappers;

import java.util.List;
import za.co.apricotdb.persistence.entity.ApricotColumn;

/**
 * The wrapper class for convenient representation of the ApricotColumn entity.
 *
 * @author Anton Nazarov
 * @since 24/10/2018
 */
public class ApricotTextColumn implements ApricotColumnWrapper {

    private ApricotColumn column;

    @Override
    public String getColumnName() {
        return column.getName();
    }

    @Override
    public String isMandatory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getOrdinalPosition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getColumnType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isForeignKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getForeignTableName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getConstraintAliases() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ApricotConstraintWrapper> getConstraints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getConstraintsAsString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
