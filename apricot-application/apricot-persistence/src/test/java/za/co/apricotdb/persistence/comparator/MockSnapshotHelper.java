package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * This helper creates a simple snapshot.
 * 
 * @author Anton Nazarov
 * @since 10/10/2019
 */
public class MockSnapshotHelper {
    
    public ApricotSnapshot createSnapshot(String name) {
        List<ApricotTable> tables = new ArrayList<>();
        ApricotSnapshot snap = new ApricotSnapshot(name, new java.util.Date(), null, null, true, null, tables);
        return snap;
    }
}
