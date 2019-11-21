package za.co.apricotdb.ui.comparator;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Generator for the alteration of the column script.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class AlterColumnScript implements CompareScriptGenerator {

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRowState() {
        return "DIFF";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.COLUMN;
    }
}
