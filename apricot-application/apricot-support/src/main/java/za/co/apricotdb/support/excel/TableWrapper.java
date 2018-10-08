package za.co.apricotdb.support.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * This class wraps the ApricotTable entity, adding the report specific
 * parameters.
 *
 * @author Anton Nazarov
 * @since 06/10/2018
 */
public class TableWrapper {

    private String tableName;
    private ApricotTable apricotTable;
    private List<ApricotRelationship> relationships;
    private Map<String, ReportRow> rows = new LinkedHashMap<>();
    private Map<String, Integer> constraintCount = new HashMap<>();
    private List<ReportRow> indexedRows = new ArrayList<>();

    public TableWrapper(ApricotTable apricotTable, List<ApricotRelationship> relationships) {
        this.apricotTable = apricotTable;
        this.relationships = relationships;
        this.tableName = apricotTable.getName();

        initConstrainCount();
        populateColumnsDefinition();
        populateConstraints();
        populateRelationships();
    }

    private void initConstrainCount() {
        constraintCount.put(ConstraintType.PRIMARY_KEY.name(), 0);
        constraintCount.put(ConstraintType.FOREIGN_KEY.name(), 0);
        constraintCount.put(ConstraintType.UNIQUE.name(), 0);
        constraintCount.put(ConstraintType.UNIQUE_INDEX.name(), 0);
        constraintCount.put(ConstraintType.NON_UNIQUE_INDEX.name(), 0);
    }

    private void populateColumnsDefinition() {
        for (ApricotColumn c : apricotTable.getColumns()) {
            ReportRow row = new ReportRow();

            row.columnName = c.getName();
            if (!c.isNullable()) {
                row.columnName += " *";
            }
            row.columnType = c.getDataType();
            if (c.getValueLength() != null && !c.getValueLength().equals("0")) {
                row.columnType += " (" + c.getValueLength() + ")";
            }
            row.isColumnDefinition = true;
            row.ordinalPosition = c.getOrdinalPosition();

            rows.put(c.getName(), row);
            indexedRows.add(row);
        }
    }

    private void populateConstraints() {
        List<ApricotConstraint> constraints = apricotTable.getConstraints();
        constraints.sort((ApricotConstraint c1, ApricotConstraint c2) -> c1.getType().getOrder() - c2.getType().getOrder());
        for (ApricotConstraint c : constraints) {
            String abbreviation = c.getType().getAbbreviation();
            int cnt = constraintCount.get(c.getType().name());
            if (cnt > 0) {
                abbreviation += String.valueOf(cnt);
            }

            for (ApricotColumnConstraint acc : c.getColumns()) {
                ReportRow row = rows.get(acc.getColumn().getName());
                if (row.constraints == null) {
                    row.constraints = abbreviation;
                } else {
                    row.constraints += ", " + abbreviation;
                }
            }
            cnt++;
            constraintCount.put(c.getType().name(), cnt);
        }
    }

    private void populateRelationships() {

        int childCnt = 0;
        int parentCnt = 0;
        int tableSize = indexedRows.size();

        for (ApricotRelationship r : relationships) {
            String child = r.getChild().getTable().getName();
            String parent = r.getParent().getTable().getName();

            if (child.equals(apricotTable.getName())) {
                if (parentCnt <= tableSize) {
                    ReportRow row = indexedRows.get(parentCnt);
                    row.parentTable = parent;
                } else {
                    ReportRow row = new ReportRow();
                    row.parentTable = parent;
                    indexedRows.add(row);
                    tableSize = indexedRows.size();
                }
                parentCnt++;
            }
            if (parent.equals(apricotTable.getName())) {
                if (childCnt <= tableSize) {
                    ReportRow row = indexedRows.get(childCnt);
                    row.childTable = child;
                } else {
                    ReportRow row = new ReportRow();
                    row.childTable = child;
                    indexedRows.add(row);
                    tableSize = indexedRows.size();
                }
                childCnt++;
            }
        }
    }

    class ReportRow {

        String parentTable;
        int ordinalPosition;
        String columnName;
        String columnType;
        String constraints;
        String childTable;
        boolean isColumnDefinition;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ReportRow> getRows() {
        return indexedRows;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (ReportRow r : indexedRows) {
            sb.append(r.parentTable).append("\t");
            sb.append(r.ordinalPosition).append("\t");
            sb.append(r.columnName).append("\t");
            sb.append(r.columnType).append("\t");
            sb.append(r.constraints).append("\t");
            sb.append(r.childTable).append("\n");
        }
        
        return sb.toString();
    }
}
