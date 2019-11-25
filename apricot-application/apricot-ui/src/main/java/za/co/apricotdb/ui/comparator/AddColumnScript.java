package za.co.apricotdb.ui.comparator;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.support.script.SqlScriptGenerator;

/**
 * Generator for the ADD COLUMN's SQL.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class AddColumnScript implements CompareScriptGenerator {

    @Autowired
    SqlScriptGenerator scriptGenerator;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        StringBuilder sb = new StringBuilder();
        List<CompareSnapshotRow> flt = filter(diffs);

        if (!flt.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--               ADD COLUMNS\n");
            sb.append("--******************************************\n");
            for (CompareSnapshotRow r : flt) {
                sb.append(scriptGenerator.addColumn((ApricotColumn) r.getDifference().getTargetObject(), schema));
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }

    @Override
    public String getRowState() {
        return "ADD";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.COLUMN;
    }

    @Override
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        return null;
    }

    @Override
    @PostConstruct
    public void init() {
        scriptGenerator.init();
    }
}
