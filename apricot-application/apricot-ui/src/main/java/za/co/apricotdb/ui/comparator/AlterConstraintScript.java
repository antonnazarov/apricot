package za.co.apricotdb.ui.comparator;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    RelatedConstraintsHandler relatedConstraintsHandler;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        return null;
    }

    @Override
    @Transactional
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        return relatedConstraintsHandler.getRelatedConstraints(filter(diffs), false);
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
