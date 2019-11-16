package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;

/**
 * The generator of the difference alignment script.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
public interface CompareScriptGenerator {

    String generate(List<CompareSnapshotRow> diffs, String schema);

    String getRowState();

    CompareRowType getRowType();

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
