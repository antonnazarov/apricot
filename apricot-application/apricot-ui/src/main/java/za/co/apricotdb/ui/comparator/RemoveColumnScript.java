package za.co.apricotdb.ui.comparator;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.support.script.SqlScriptGenerator;

/**
 * Generator for the DROP COLUMN's SQL.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class RemoveColumnScript implements CompareScript {

    @Autowired
    SqlScriptGenerator scriptGenerator;

    @Autowired
    RelatedConstraintsHandler relConstrHandler;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        StringBuilder sb = new StringBuilder();
        List<CompareSnapshotRow> flt = filter(diffs);

        if (!flt.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--               DROP COLUMNS\n");
            sb.append("--******************************************\n");
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
     * Get all constraints, related to the columns, which are about to be DROPPED.
     */
    @Override
    @Transactional
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        List<CompareSnapshotRow> flt = filter(diffs);

        return relConstrHandler.getConstraintsRelatedToColumn(flt, true);
    }

    @Override
    @PostConstruct
    public void init() {
        scriptGenerator.init();
    }
}
