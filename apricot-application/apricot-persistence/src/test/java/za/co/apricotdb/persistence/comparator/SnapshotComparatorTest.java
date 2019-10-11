package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test for SnapshotComparator.
 *  
 * @author Anton Nazarov
 * @since 10/10/2019
 */
public class SnapshotComparatorTest {
    
    SnapshotComparator snapComp = null;
    ApricotSnapshot snapSource = null;
    ApricotSnapshot snapTarget = null;
    MockTableHelper mockTableHelper = null;
    TableComparator tableComparator = null;
    RelationshipComparator relationshipComparator = null;
    
    @Before
    public void setUp() {
        snapComp = new SnapshotComparator();
        tableComparator = Mockito.mock(TableComparator.class);
        relationshipComparator = Mockito.mock(RelationshipComparator.class);
        when(tableComparator.compare(any(ApricotTable.class), any(ApricotTable.class))).then
        
        mockTableHelper = new MockTableHelper();
        Map<TestTable, ApricotTable> tbl = mockTableHelper.createTables();
        snapSource = new ApricotSnapshot("SOURCE_SNAPSHOT", null, null, null, true, null, new ArrayList<>(tbl.values()));
        snapTarget = new ApricotSnapshot("TARGET_SNAPSHOT", null, null, null, true, null, new ArrayList<>(tbl.values()));
    }
    
    @Test
    public void testCompare() {
        // snapComp.
    }
    
}
