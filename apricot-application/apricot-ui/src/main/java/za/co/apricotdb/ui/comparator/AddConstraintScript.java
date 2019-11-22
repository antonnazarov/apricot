package za.co.apricotdb.ui.comparator;

import java.util.List;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * Generator for the ADD CONSTRAINT SQL for the tables newly added into the target
 * snapshot.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class AddConstraintScript implements CompareScriptGenerator {

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRowState() {
        return "ADD";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.CONSTRAINT;
    }

    @Override
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        return null;
    }
}
