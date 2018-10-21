package za.co.apricotdb.support.excel;

import org.junit.Before;
import org.junit.Test;

public class TableWrapperTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testShortTable() {
        TableWrapper w = TableWrapperHelper.getTableWrapper();
        System.out.println(w);
    }
}
