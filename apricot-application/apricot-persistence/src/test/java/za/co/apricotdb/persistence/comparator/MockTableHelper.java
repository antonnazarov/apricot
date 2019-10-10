package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * Creates A table.
 * 
 * @author Anton Nazarov
 * @since 10/10/2019
 */
public class MockTableHelper {
    
    public ApricotTable createTable(String name) {
        List<ApricotColumn> cls = new ArrayList<>();
        ApricotColumn col1 = new ApricotColumn("FLD1", 1, false, "INT", null, null);
        ApricotColumn col2 = new ApricotColumn("FLD_COMMENT", 2, false, "VARCHAR", "20", null);
        cls.add(col1);
        cls.add(col2);
        List<ApricotConstraint> cnstrs = new ArrayList<>();
        ApricotConstraint cn = new ApricotConstraint("PK1", ConstraintType.PRIMARY_KEY, null);
        ApricotTable ret = new ApricotTable(name, cls, cnstrs, null);
        
        return ret;
    }
}
