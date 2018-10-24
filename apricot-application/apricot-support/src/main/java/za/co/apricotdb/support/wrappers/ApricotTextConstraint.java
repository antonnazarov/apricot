package za.co.apricotdb.support.wrappers;

import za.co.apricotdb.persistence.entity.ApricotConstraint;

public class ApricotTextConstraint implements ApricotConstraintWrapper {
    private ApricotConstraint constraint;
    private String alias;

    @Override
    public String getAlias() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getConstraintName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
