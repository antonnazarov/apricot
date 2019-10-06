package za.co.apricotdb.persistence.comparator;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotTable;

public class TableDifference implements ApricotObjectDifference<ApricotTable> {

    private ApricotTable source;
    private ApricotTable target;
    private List<ColumnDifference> columnDiffs;
    private List<ConstraintDifference> constraintDiffs;

    public TableDifference(ApricotTable source, ApricotTable target) {
        this.source = source;
        this.target = target;
        columnDiffs = new ArrayList<>();
        constraintDiffs = new ArrayList<>();
    }

    public List<ColumnDifference> getColumnDiffs() {
        return columnDiffs;
    }

    public List<ConstraintDifference> getConstraintDiffs() {
        return constraintDiffs;
    }

    @Override
    public ApricotTable getSourceObject() {
        return source;
    }

    @Override
    public ApricotTable getTargetObject() {
        return target;
    }

    @Override
    public boolean isDifferent() {
        return source != null && target != null && columnDiffs.isEmpty() && constraintDiffs.isEmpty();
    }
}
