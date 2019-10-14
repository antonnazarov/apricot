package za.co.apricotdb.persistence.comparator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

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
        snapComp.tableComparator = tableComparator;
        snapComp.relationshipComparator = relationshipComparator;

        mockTableHelper = new MockTableHelper();
        Map<TestTable, ApricotTable> tbl = mockTableHelper.createTables();
        snapSource = new ApricotSnapshot("SOURCE_SNAPSHOT", null, null, null, true, null,
                new ArrayList<>(tbl.values()));
        snapTarget = new ApricotSnapshot("TARGET_SNAPSHOT", null, null, null, true, null,
                new ArrayList<>(tbl.values()));

        when(tableComparator.compare(tbl.get(TestTable.ACCOUNT), tbl.get(TestTable.ACCOUNT)))
                .thenReturn(new TableDifference(tbl.get(TestTable.ACCOUNT), tbl.get(TestTable.ACCOUNT)));
        
        List<TableDifference> ltd = new ArrayList<>();
        ltd.add(new TableDifference(tbl.get(TestTable.ACCOUNT), tbl.get(TestTable.ACCOUNT)));
        List<RelationshipDifference> rds = new ArrayList<>();
        rds.add(new RelationshipDifference(null, null));
        when(relationshipComparator.compare(snapSource, snapTarget, ltd)).thenReturn(rds);
    }

    @Test
    public void testCompare() {
        SnapshotDifference snapDiff = snapComp.compare(snapSource, snapTarget);
        assertNotNull(snapDiff);
        assertTrue(!snapDiff.getTableDiffs().isEmpty());
        
        System.out.println(snapDiff);
    }
}
