package za.co.apricotdb.persistence.comparator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.TableCloneManager;
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

    @Autowired
    RelationshipComparator relationshipComparator;

    @Autowired
    TableCloneManager tableCloneManager;

    @Override
    @Transactional
    public SnapshotDifference compare(ApricotSnapshot source, ApricotSnapshot target) {
        SnapshotDifference diff = new SnapshotDifference(source, target);
        compareTables(diff);
        
        if (diff.isDifferent()) {
            sortTablesByDiffType(diff.getTableDiffs());
        }

        return diff;
    }

    private void compareTables(SnapshotDifference diff) {
        List<ApricotTable> sourceTables = sortTables(diff.getSourceObject().getTables());
        List<ApricotTable> targetTables = sortTables(diff.getTargetObject().getTables());

        for (ApricotTable srcTable : sourceTables) {
            ApricotTable trgtTable = targetTables.stream().filter(trgt -> trgt.equals(srcTable)).findFirst()
                    .orElse(null);
            TableDifference td = null;
            if (trgtTable == null) {
                td = new TableDifference(clone(srcTable), null);
            } else {
                td = tableComparator.compare(clone(srcTable), clone(trgtTable));
            }
            if (td != null) {
                diff.getTableDiffs().add(td);
            }
        }

        for (ApricotTable trgtTable : targetTables) {
            ApricotTable srcTable = sourceTables.stream().filter(src -> src.equals(trgtTable)).findFirst().orElse(null);
            if (srcTable == null) {
                TableDifference td = new TableDifference(null, clone(trgtTable));
                diff.getTableDiffs().add(td);
            }
        }
    }

    private List<ApricotTable> sortTables(List<ApricotTable> tables) {
        List<ApricotTable> ret = tables.stream().sorted(Comparator.comparing(ApricotTable::getName))
                .collect(Collectors.toList());
        return ret;
    }

    public void sortTablesByDiffType(List<TableDifference> tableDiffs) {
        tableDiffs.sort((TableDifference d1, TableDifference d2) -> {
            return d1.getOrderWeight().compareTo(d2.getOrderWeight());
        });
    }

    private ApricotTable clone(ApricotTable table) {
        ApricotTable tbl = tableCloneManager.cloneTable(null, table, true, false, true);

        return tbl;
    }
}
