package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ConstraintManager;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.support.script.GenericScriptGenerator;

/**
 * Generator for the DROP COLUMN's SQL.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class RemoveColumnScript implements CompareScriptGenerator {

    @Autowired
    GenericScriptGenerator scriptGenerator;

    @Autowired
    ConstraintManager constraintManager;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        StringBuilder sb = new StringBuilder();
        List<CompareSnapshotRow> flt = filter(diffs);

        if (!flt.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--               DROP COLUMNS\n");
            sb.append("--******************************************\n\n");
            for (CompareSnapshotRow r : flt) {
                sb.append(scriptGenerator.dropColumn((ApricotColumn) r.getDifference().getSourceObject(), schema));
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }

    @Override
    public String getRowState() {
        return "REMOVE";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.COLUMN;
    }

    /**
     * Get all the constraints, related to the columns, which are about to be
     * DROPPED.
     */
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        List<ApricotConstraint> ret = new ArrayList<>();

        List<CompareSnapshotRow> flt = filter(diffs);
        for (CompareSnapshotRow r : flt) {
            ApricotColumn column = (ApricotColumn) r.getDifference().getSourceObject();
            List<ApricotConstraint> constraints = constraintManager.getConstraintsByColumn(column);
            if (constraints != null && !constraints.isEmpty()) {
                ret.addAll(constraints);
            }
        }

        return ret;
    }
}
