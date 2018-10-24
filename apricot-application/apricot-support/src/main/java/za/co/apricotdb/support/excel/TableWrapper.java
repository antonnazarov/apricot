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

    private ApricotTable apricotTable;
    private List<ApricotRelationship> relationships;
    private Map<String, ReportRow> rows = new LinkedHashMap<>();
    private List<ReportRow> indexedRows = new ArrayList<>();
    private Map<String, String> constraintsLegend = new LinkedHashMap<>();

    public TableWrapper(ApricotTable apricotTable, List<ApricotRelationship> relationships) {
        this.apricotTable = apricotTable;
        this.relationships = relationships;

        populateColumnsDefinition();
        populateConstraints();
        populateRelationships();
    }

    private void initConstrainCount(Map<String, Integer> constraintCount) {
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
        Map<String, Integer> constraintCount = new HashMap<>();
        initConstrainCount(constraintCount);

        List<ApricotConstraint> constraints = apricotTable.getConstraints();
        constraints.sort((ApricotConstraint c1, ApricotConstraint c2) -> {
            if (c1.getType().getOrder() == c2.getType().getOrder()) {
                //  use ordinal position of the constraint fields
                return c1.getColumns().get(0).getColumn().getOrdinalPosition() - c2.getColumns().get(0).getColumn().getOrdinalPosition();
            }
            return c1.getType().getOrder() - c2.getType().getOrder();
        });

        for (ApricotConstraint c : constraints) {
            String abbreviation = c.getType().getAbbreviation();
            int cnt = constraintCount.get(c.getType().name());
            if (cnt > 0) {
                abbreviation += String.valueOf(cnt);
            }

            //  populate the constraint legend
            constraintsLegend.put(abbreviation, c.getName());

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
        int tableSize = indexedRows.size();

        relationships.sort((ApricotRelationship r1, ApricotRelationship r2) -> r1.getChild().getTable().getName().compareTo(r2.getChild().getTable().getName()));
        List<String> children = new ArrayList<>();
        for (ApricotRelationship r : relationships) {
            String child = r.getChild().getTable().getName();
            String parent = r.getParent().getTable().getName();

            //  populate the parent tables names
            if (child.equals(apricotTable.getName())) {
                List<ApricotColumnConstraint> relColumns = r.getChild().getColumns();
                for (ApricotColumnConstraint acc : relColumns) {
                    ReportRow row = rows.get(acc.getColumn().getName());
                    row.parentTable = parent;
                }
            }

            //  populate the child tables names
            if (parent.equals(apricotTable.getName())) {
                if (!children.contains(child)) {
                    if (childCnt < tableSize) {
                        ReportRow row = indexedRows.get(childCnt);
                        row.childTable = child;
                    } else {
                        ReportRow row = new ReportRow();
                        row.childTable = child;
                        indexedRows.add(row);
                        tableSize = indexedRows.size();
                    }
                    children.add(child);
                    childCnt++;
                }
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
        return apricotTable.getName();
    }

    public List<ReportRow> getRows() {
        return indexedRows;
    }

    public Map<String, String> getConstraintsLegend() {
        return constraintsLegend;
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
