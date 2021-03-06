package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The comparator of two ApricotRelationship's.
 * 
 * @author Anton Nazarov
 * @since 08/10/2019
 */
@Component
public class RelationshipComparator implements ApricotObjectComparator<ApricotRelationship, RelationshipDifference> {

    @Autowired
    RelationshipManager relationshipManager;

    @Transactional
    public List<RelationshipDifference> compare(ApricotSnapshot source, ApricotSnapshot target,
            List<TableDifference> tableDiff) {
        List<RelationshipDifference> ret = new ArrayList<>();

        List<ApricotRelationship> sourceRls = relationshipManager.findRelationshipsBySnapshot(source);
        List<ApricotRelationship> targetRls = relationshipManager.findRelationshipsBySnapshot(target);
        List<ConstraintDifference> constraintDiffs = getConstraintDiffs(tableDiff);

        for (ApricotRelationship srcr : sourceRls) {
            ApricotRelationship trgtr = targetRls.stream()
                    .filter(trgt -> trgt.getParent().getTable().getName().equalsIgnoreCase(srcr.getParent().getTable().getName())
                            && trgt.getChild().getTable().getName().equalsIgnoreCase(srcr.getChild().getTable().getName()))
                    .findFirst().orElse(null);
            if (trgtr == null) {
                RelationshipDifference diff = new RelationshipDifference(srcr, trgtr);
                ret.add(diff);
            } else {
                RelationshipDifference diff = compare(srcr, trgtr, constraintDiffs);
                ret.add(diff);
            }
        }

        for (ApricotRelationship trgtr : targetRls) {
            ApricotRelationship srcr = sourceRls.stream()
                    .filter(src -> src.getParent().getTable().getName().equalsIgnoreCase(trgtr.getParent().getTable().getName())
                            && src.getChild().getTable().getName().equalsIgnoreCase(trgtr.getChild().getTable().getName()))
                    .findFirst().orElse(null);
            if (srcr == null) {
                RelationshipDifference diff = new RelationshipDifference(srcr, trgtr);
                ret.add(diff);
            }
        }

        return ret;
    }

    public RelationshipDifference compare(ApricotRelationship source, ApricotRelationship target,
            List<ConstraintDifference> constraintDiffs) {

        ConstraintDifference pkDiff = findConstraintDifference(source.getParent(), constraintDiffs, true);
        ConstraintDifference fkDiff = findConstraintDifference(source.getChild(), constraintDiffs, true);

        RelationshipDifference ret = new RelationshipDifference(source, target);
        ret.setPkDiff(pkDiff);
        ret.setFkDiff(fkDiff);

        return ret;
    }

    private List<ConstraintDifference> getConstraintDiffs(List<TableDifference> tableDiff) {
        List<ConstraintDifference> ret = new ArrayList<>();

        for (TableDifference td : tableDiff) {
            ret.addAll(td.getConstraintDiffs());
        }

        return ret;
    }

    /**
     * Do not implement this method.
     */
    @Override
    public RelationshipDifference compare(ApricotRelationship source, ApricotRelationship target) {
        return null;
    }

    private ConstraintDifference findConstraintDifference(ApricotConstraint constraint,
            List<ConstraintDifference> constraintDiffs, boolean source) {
        ConstraintDifference ret = null;

        for (ConstraintDifference cd : constraintDiffs) {
            if (source) {
                if (cd.getSourceObject() != null && cd.getSourceObject().equals(constraint)) {
                    ret = cd;
                    break;
                }
            } else {
                if (cd.getTargetObject() != null && cd.getTargetObject().equals(constraint)) {
                    ret = cd;
                    break;
                }
            }
        }

        return ret;
    }
}
