package za.co.apricotdb.persistence.comparator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The comparator of the Apricot Snapshots.
 * 
 * @author Anton Nazarov
 * @since 06/10/2019
 */
@Component
public class SnapshotComparator implements ApricotObjectComparator<ApricotSnapshot, SnapshotDifference> {

    @Autowired
    TableComparator tableComparator;

    @Override
    public SnapshotDifference compare(ApricotSnapshot source, ApricotSnapshot target) {
        SnapshotDifference diff = new SnapshotDifference(source, target);
        compareTables(diff);

        return diff;
    }

    private void compareTables(SnapshotDifference diff) {
        List<ApricotTable> sourceTables = diff.getSourceObject().getTables();
        List<ApricotTable> targetTables = diff.getTargetObject().getTables();

        for (ApricotTable srcTable : sourceTables) {
            ApricotTable trgtTable = targetTables.stream().filter(trgt -> trgt.equals(srcTable)).findFirst()
                    .orElse(null);
            TableDifference td = null;
            if (trgtTable == null) {
                td = new TableDifference(srcTable, trgtTable);
            } else {
                td = tableComparator.compare(srcTable, trgtTable);
            }
            diff.getTableDiffs().add(td);
        }

        for (ApricotTable trgtTable : targetTables) {
            ApricotTable srcTable = targetTables.stream().filter(trgt -> trgt.equals(trgtTable)).findFirst()
                    .orElse(null);
            if (srcTable == null) {
                TableDifference td = new TableDifference(srcTable, trgtTable);
                diff.getTableDiffs().add(td);
            }
        }
    }
}
