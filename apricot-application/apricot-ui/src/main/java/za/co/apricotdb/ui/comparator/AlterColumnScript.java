package za.co.apricotdb.ui.comparator;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.support.script.GenericScriptGenerator;

/**
 * Generator for the alteration of the column script.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class AlterColumnScript implements CompareScriptGenerator {

    @Autowired
    GenericScriptGenerator scriptGenerator;

    @Autowired
    RelatedConstraintsHandler relConstrHandler;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        StringBuilder sb = new StringBuilder();
        List<CompareSnapshotRow> flt = filter(diffs);

        if (!flt.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--               ALTER COLUMNS\n");
            sb.append("--******************************************\n");
            for (CompareSnapshotRow r : flt) {
                sb.append(scriptGenerator.dropColumn((ApricotColumn) r.getDifference().getSourceObject(), schema));
                sb.append("\n\n");
                sb.append(scriptGenerator.addColumn((ApricotColumn) r.getDifference().getTargetObject(), schema));
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }

    @Override
    public String getRowState() {
        return "DIFF";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.COLUMN;
    }

    /**
     * Get all constraints, related to the columns, which are about to be ALTERED.
     * They will be eligible for recover after the columns have been altered.
     */
    @Override
    @Transactional
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        List<CompareSnapshotRow> flt = filter(diffs);

        return relConstrHandler.getConstraintsRelatedToColumn(flt, false);
    }
}
