package za.co.apricotdb.persistence.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * This manager scans the table and related objects, creating the fresh copy of
 * the whole structure, ready for serialization.
 * 
 * @author Anton Nazarov
 * @since 15/02/2019
 *
 */
@Component
public class TableCloneManager {

    @Autowired
    TableManager tableManager;

    @Autowired
    ConstraintManager constraintManager;

    /**
     * Clone the table.
     */
    public ApricotTable cloneTable(ApricotSnapshot newSnapshot, ApricotTable table, boolean cloneConstraints,
            boolean generateName) {
        List<ApricotColumn> clonedColumns = new ArrayList<>();
        List<ApricotConstraint> clonedConstraints = new ArrayList<>();

        String name = table.getName();
        if (generateName) {
            while (!isUniqueTable(name)) {
                name = ApricotNameUtil.generateSeqUniqueName(name);
            }
        }

        ApricotTable clonedTable = new ApricotTable(name, clonedColumns, clonedConstraints, newSnapshot);

        for (ApricotColumn column : table.getColumns()) {
            clonedColumns.add(cloneColumn(clonedTable, column));
        }

        if (cloneConstraints) {
            for (ApricotConstraint constraint : table.getConstraints()) {
                clonedConstraints.add(cloneConstraint(clonedTable, constraint, false));
            }
        }

        return clonedTable;
    }

    private boolean isUniqueTable(String name) {
        if (tableManager.getTableByName(name) == null) {
            return true;
        }

        return false;
    }

    private boolean isUniqueConstraint(String name) {
        if (constraintManager.getConstraintByName(name) == null) {
            return true;
        }

        return false;
    }

    /**
     * Clone the column.
     */
    private ApricotColumn cloneColumn(ApricotTable clonedTable, ApricotColumn column) {
        ApricotColumn clonedColumn = new ApricotColumn(column.getName(), column.getOrdinalPosition(),
                column.isNullable(), column.getDataType(), column.getValueLength(), clonedTable);

        return clonedColumn;
    }

    /**
     * Clone the constraint.
     */
    public ApricotConstraint cloneConstraint(ApricotTable clonedTable, ApricotConstraint constraint,
            boolean generateName) {
        String name = constraint.getName();
        if (generateName) {
            while (!isUniqueConstraint(name)) {
                name = ApricotNameUtil.generateSeqUniqueName(name);
            }
        }

        ApricotConstraint clonedConstraint = new ApricotConstraint(name, constraint.getType(), clonedTable);
        List<ApricotColumnConstraint> clonedColumnConstraints = new ArrayList<>();
        clonedConstraint.setColumns(clonedColumnConstraints);

        for (ApricotColumnConstraint cc : constraint.getColumns()) {
            clonedColumnConstraints.add(cloneColumnConstraint(clonedTable, clonedConstraint, cc));
        }

        return clonedConstraint;
    }

    /**
     * Clone the column constraint.
     */
    private ApricotColumnConstraint cloneColumnConstraint(ApricotTable clonedTable, ApricotConstraint clonedConstraint,
            ApricotColumnConstraint columnConstraint) {
        ApricotColumn clonedColumn = getColumnByName(clonedTable, columnConstraint.getColumn().getName());
        ApricotColumnConstraint clonedColumnConstraint = new ApricotColumnConstraint(clonedConstraint, clonedColumn);
        clonedColumnConstraint.setOrdinalPosition(columnConstraint.getOrdinalPosition());

        return clonedColumnConstraint;
    }

    private ApricotColumn getColumnByName(ApricotTable clonedTable, String columnName) {
        for (ApricotColumn c : clonedTable.getColumns()) {
            if (columnName.equals(c.getName())) {
                return c;
            }
        }

        return null;
    }
}
