package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * Generator for the DROP CONSTRAINT SQL for the tables newly added into the
 * target snapshot.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class RemoveConstraintScript implements CompareScriptGenerator {
    
    @Autowired
    ConstraintManager constraintManager;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
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

    @Override
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        List<ApricotConstraint> ret = new ArrayList<>();
        List<CompareSnapshotRow> flt = filter(diffs);

        for (CompareSnapshotRow r : flt) {
            ApricotConstraint constraint = (ApricotConstraint) r.getDifference().getSourceObject();
            constraint = constraintManager.getConstraintById(constraint.getId());
            if (constraint != null) {
                ret.add(constraint);
            }
        }

        return ret;
    }
}
