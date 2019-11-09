package za.co.apricotdb.ui.comparator;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Generator for the DROP CONSTRAINT SQL for the tables newly added into the target
 * snapshot.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class RemoveConstraintScript implements CompareScriptGenerator {

    @Override
    public String generate(List<CompareSnapshotRow> diffs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRowState() {
        return "REMOVE";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.CONSTRAINT;
    }
}
