package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * Creates the collection of the testing tables.
 * 
 * @author Anton Nazarov
 * @since 10/10/2019
 */
public class MockTableHelper {
    
    public Map<TestTable, ApricotTable> createTables() {
        
        Map<TestTable, ApricotTable> ret = new HashMap<>(); 
        
        List<ApricotColumn> cls = new ArrayList<>();
        ApricotColumn col1 = new ApricotColumn("ACCOUNT_ID", 1, false, "INT", null, null);
        ApricotColumn col2 = new ApricotColumn("ACCOUNT_NAME", 2, false, "VARCHAR", "20", null);
        ApricotColumn col3 = new ApricotColumn("ACCOUNT_COMMENT", 3, true, "VARCHAR", "1000", null);
        ApricotColumn col4 = new ApricotColumn("ACCOUNT_TYPE", 4, false, "LONG", null, null);  //  the foreign key
        cls.add(col1);
        cls.add(col2);
        cls.add(col3);
        cls.add(col4);
        List<ApricotConstraint> cnstrs = new ArrayList<>();
        ApricotTable account = new ApricotTable(TestTable.ACCOUNT.name(), cls, cnstrs, null);
        ApricotConstraint cn1 = new ApricotConstraint("PK1", ConstraintType.PRIMARY_KEY, account, "ACCOUNT_ID");
        ApricotConstraint cn2 = new ApricotConstraint("FK_ACCOUNT_TYPE", ConstraintType.FOREIGN_KEY, account, "ACCOUNT_TYPE");
        ApricotConstraint cn3 = new ApricotConstraint("UNIQIE_ACCOUNT_NAME", ConstraintType.UNIQUE_INDEX, account, "ACCOUNT_NAME");
        cnstrs.add(cn1);
        cnstrs.add(cn2);
        cnstrs.add(cn3);
        ret.put(TestTable.ACCOUNT, account);
        
        cls = new ArrayList<>();
        col1 = new ApricotColumn("ACCOUNT_REF_ID", 1, false, "LONG", null, null);
        col2 = new ApricotColumn("ACCOUNT_REF_NAME", 2, false, "VARCHAR", "45", null);
        cls.add(col1);
        cls.add(col2);
        cnstrs = new ArrayList<>();
        ApricotTable accountRef = new ApricotTable(TestTable.ACCOUNT_REF.name(), cls, cnstrs, null);
        cn1 = new ApricotConstraint("PK_ACCOUNT_REF", ConstraintType.PRIMARY_KEY, accountRef, "ACCOUNT_REF_ID");
        cnstrs.add(cn1);
        ret.put(TestTable.ACCOUNT_REF, accountRef);
        
        cls = new ArrayList<>();
        col1 = new ApricotColumn("STD_ID", 1, false, "LONG", null, null);
        col2 = new ApricotColumn("STD_AMOUNT", 2, false, "DOUBLE", null, null);
        col3 = new ApricotColumn("STD_NAME", 3, false, "VARCHAR", "30", null);
        col4 = new ApricotColumn("STD_COMMENT", 4, true, "VARCHAR", "450", null);
        cls.add(col1);
        cls.add(col2);
        cls.add(col3);
        cls.add(col4);
        cnstrs = new ArrayList<>();
        ApricotTable standalone = new ApricotTable(TestTable.STANDALONE.name(), cls, cnstrs, null);
        cn1 = new ApricotConstraint("PK_STANDALONE", ConstraintType.PRIMARY_KEY, standalone, "STD_ID");
        cn2 = new ApricotConstraint("UNIQUE_NAME", ConstraintType.UNIQUE_INDEX, standalone, "STD_NAME");
        cnstrs.add(cn1);
        cnstrs.add(cn2);
        ret.put(TestTable.ACCOUNT_REF, standalone);
        
        return ret;
    }
}
