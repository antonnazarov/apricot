package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * The generator of the difference alignment script.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
public interface CompareScriptGenerator {

    String generate(List<CompareSnapshotRow> diffs, String schema);
    
    List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs);    

    String getRowState();

    CompareRowType getRowType();
    
    void init();

    default List<CompareSnapshotRow> filter(List<CompareSnapshotRow> diffs) {
        List<CompareSnapshotRow> ret = new ArrayList<>();

        for (CompareSnapshotRow r : diffs) {
            if (r.getState().toString().equals(getRowState()) && r.getType() == getRowType()) {
                ret.add(r);
            }
        }

        return ret;
    }
}
