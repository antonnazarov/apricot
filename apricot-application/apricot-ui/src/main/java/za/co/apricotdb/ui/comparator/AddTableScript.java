package za.co.apricotdb.ui.comparator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.support.script.GenericScriptGenerator;

/**
 * Generator for the CREATE TABLE SQL for the tables newly added into the target
 * snapshot.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class AddTableScript implements CompareScriptGenerator {

    @Autowired
    GenericScriptGenerator scriptGenerator;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        StringBuilder sb = new StringBuilder();
        List<CompareSnapshotRow> flt = filter(diffs);

        if (!flt.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--               NEW TABLES\n");
            sb.append("--******************************************\n");
            for (CompareSnapshotRow r : flt) {
                sb.append(scriptGenerator.createTable((ApricotTable) r.getDifference().getTargetObject(), schema));
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
        return CompareRowType.TABLE;
    }
}
