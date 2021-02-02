package za.co.apricotdb.persistence.comparator;

import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

import javax.transaction.Transactional;
import java.util.List;

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

        //  02/02/2020 merge the Primary Key
        ConstraintDifference pk1 = null;
        ConstraintDifference pk2 = null;
        for (ConstraintDifference cd : diff.getConstraintDiffs()) {
            if (cd.isDifferent() && cd.getSourceObject() != null && cd.getSourceObject().getType() == ConstraintType.PRIMARY_KEY) {
                pk1 = cd;
            }
            if (cd.isDifferent() && cd.getTargetObject() != null && cd.getTargetObject().getType() == ConstraintType.PRIMARY_KEY) {
                pk2 = cd;
            }
        }
        if (pk1 != null && pk2 != null) {
            ConstraintDifference newCd = new ConstraintDifference(pk1.getSourceObject(), pk2.getTargetObject());
            if (!newCd.isDifferent()) {
                diff.getConstraintDiffs().remove(pk1);
                diff.getConstraintDiffs().remove(pk2);
                diff.getConstraintDiffs().add(newCd);
            }
        }
    }
}
