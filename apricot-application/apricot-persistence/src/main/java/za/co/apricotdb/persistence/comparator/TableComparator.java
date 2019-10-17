package za.co.apricotdb.persistence.comparator;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * The comparator of the Apricot Tables.
 * 
 * @author Anton Nazarov
 * @since 06/10/2019
 */
@Component
public class TableComparator implements ApricotObjectComparator<ApricotTable, TableDifference> {

    @Override
    @Transactional
    public TableDifference compare(ApricotTable source, ApricotTable target) {
        TableDifference diff = new TableDifference(source, target);
        compareFields(diff);
        compareConstraints(diff);

        return diff;
    }

    private void compareFields(TableDifference diff) {
        List<ApricotColumn> scrColumns = diff.getSourceObject().getColumns();
        List<ApricotColumn> trgtColumns = diff.getTargetObject().getColumns();

        for (ApricotColumn srcCol : scrColumns) {
            ApricotColumn trgtCol = trgtColumns.stream().filter(trgt -> trgt.equals(srcCol)).findFirst().orElse(null);
            ColumnDifference cd = new ColumnDifference(srcCol, trgtCol);
            diff.getColumnDiffs().add(cd);
        }

        for (ApricotColumn trgtCol : trgtColumns) {
            ApricotColumn srcCol = scrColumns.stream().filter(src -> src.equals(trgtCol)).findFirst().orElse(null);
            if (srcCol == null) {
                ColumnDifference cd = new ColumnDifference(srcCol, trgtCol);
                diff.getColumnDiffs().add(cd);
            }
        }
    }

    private void compareConstraints(TableDifference diff) {
        List<ApricotConstraint> scrConstrs = diff.getSourceObject().getConstraints();
        List<ApricotConstraint> trgConstrs = diff.getTargetObject().getConstraints();

        for (ApricotConstraint srcCnt : scrConstrs) {
            ApricotConstraint trgtCnt = trgConstrs.stream().filter(trgt -> trgt.equals(srcCnt)).findFirst()
                    .orElse(null);
            ConstraintDifference cd = new ConstraintDifference(srcCnt, trgtCnt);
            diff.getConstraintDiffs().add(cd);
        }

        for (ApricotConstraint trgtCnt : trgConstrs) {
            ApricotConstraint srcCnt = scrConstrs.stream().filter(src -> src.equals(trgtCnt)).findFirst().orElse(null);
            if (srcCnt == null) {
                ConstraintDifference cd = new ConstraintDifference(srcCnt, trgtCnt);
                diff.getConstraintDiffs().add(cd);
            }
        }
    }
}
