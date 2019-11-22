package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.support.script.GenericScriptGenerator;

/**
 * Generator for the DROP TABLE SQL for the tables newly added into the target
 * snapshot.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class RemoveTableScript implements CompareScriptGenerator {

    @Autowired
    GenericScriptGenerator scriptGenerator;

    @Autowired
    TableManager tableManager;

    @Override
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        StringBuilder sb = new StringBuilder();
        List<CompareSnapshotRow> flt = filter(diffs);

        if (!flt.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--               DROP TABLES\n");
            sb.append("--******************************************\n");
            List<ApricotTable> tables = new ArrayList<>();
            for (CompareSnapshotRow r : flt) {
                ApricotTable tbl = (ApricotTable) r.getDifference().getSourceObject();
                tables.add(tableManager.getTableById(tbl.getId()));
            }

            sb.append(scriptGenerator.dropSelectedTables(tables, schema));
        }

        return sb.toString();
    }

    @Override
    public String getRowState() {
        return "REMOVE";
    }

    @Override
    public CompareRowType getRowType() {
        return CompareRowType.TABLE;
    }

    @Override
    public List<ApricotConstraint> getRelatedConstraints(List<CompareSnapshotRow> diffs) {
        return null;
    }
}
