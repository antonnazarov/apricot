package za.co.apricotdb.ui.comparator;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * Generator for the DROP CONSTRAINT SQL for the tables newly added into the
 * target snapshot.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class RemoveConstraintScript implements CompareScript {

    @Autowired
    RelatedConstraintsHandler relatedConstraintsHandler;

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
        return relatedConstraintsHandler.getRelatedConstraints(filter(diffs), true);
    }
    
    @Override
    @PostConstruct
    public void init() {
    }
}
