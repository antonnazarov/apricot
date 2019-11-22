package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * The script generator for the constraint alteration case.
 * 
 * @author Anton Nazarov
 * @since 22/11/2019
 */
@Component
public class AlterConstraintScript implements CompareScriptGenerator {

    @Autowired
    ConstraintManager constraintManager;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        return null;
    }

    @Override
    @Transactional
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        List<ApricotConstraint> ret = new ArrayList<>();
        List<CompareSnapshotRow> flt = filter(diffs);

        for (CompareSnapshotRow r : flt) {
            ApricotConstraint constraint = (ApricotConstraint) r.getDifference().getTargetObject();
            constraint = constraintManager.getConstraintById(constraint.getId());
            if (constraint != null) {
                ret.add(constraint);
            }
        }

        return ret;
    }

    @Override
    public String getRowState() {
        return "DIFF";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.CONSTRAINT;
    }
}
