package za.co.apricotdb.ui.comparator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.support.script.GenericScriptGenerator;

/**
 * Generator for the ADD COLUMN's SQL.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class AddColumnScript implements CompareScriptGenerator {

    @Autowired
    GenericScriptGenerator scriptGenerator;

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

}
