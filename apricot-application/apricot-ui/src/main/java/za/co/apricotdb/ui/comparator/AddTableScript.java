package za.co.apricotdb.ui.comparator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.TableManager;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.support.script.SqlScriptGenerator;

/**
 * Generator for the CREATE TABLE SQL for the tables newly added into the target
 * snapshot.
 * 
 * @author Anton Nazarov
 * @since 09/11/2019
 */
@Component
public class AddTableScript implements CompareScript {

    @Autowired
    SqlScriptGenerator scriptGenerator;

    @Autowired
    TableManager tableManager;

    @Override
    @Transactional
    public String generate(List<CompareSnapshotRow> diffs, String schema) {
        StringBuilder sb = new StringBuilder();
        List<CompareSnapshotRow> flt = filter(diffs);

        if (!flt.isEmpty()) {
            sb.append("--******************************************\n");
            sb.append("--             THE NEW TABLES               \n");
            sb.append("--******************************************\n");
            List<ApricotConstraint> constraints = new ArrayList<>();
            for (CompareSnapshotRow r : flt) {
                ApricotTable table = tableManager
                        .getTableById(((ApricotTable) r.getDifference().getTargetObject()).getId());
                sb.append(scriptGenerator.createTable(table, schema, false));
                constraints.addAll(table.getConstraints());
            }

            sb.append("--******************************************\n");
            sb.append("--    THE CONSTRAINTS OF THE NEW TABLES     \n");
            sb.append("--******************************************\n");
            sb.append(scriptGenerator.createConstraints(constraints, schema, true));
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
