package za.co.apricotdb.ui.comparator;

import java.util.List;

/**
 * The generator of the difference alignment script.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
public interface CompareScriptGenerator {

    String generate(List<CompareSnapshotRow> diffs);
    
    String getRowState();
    
    CompareRowType getRowType(); 
}
