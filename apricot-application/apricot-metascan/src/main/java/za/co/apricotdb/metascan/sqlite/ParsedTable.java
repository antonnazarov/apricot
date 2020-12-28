package za.co.apricotdb.metascan.sqlite;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.create.table.NamedConstraint;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This bean contains data, extracted from the CreateTable data parser.
 *
 * @author Anton Nazarov
 * @since 22/09/2020
 */
public class ParsedTable {

    private ApricotTable table;
    private CreateTable createTable;
    private List<ForeignKeyIndex> foreignKeys = new ArrayList<>();
    private ApricotSnapshot snapshot;

    public static ParsedTable parseTable(CreateTable createTable, ApricotSnapshot snapshot,
                                         ParsedBundle parsedBundle) {
        ParsedTable parsed = new ParsedTable(createTable, snapshot);
        parsed.parseTable(parsedBundle);

        return parsed;
    }

    private ParsedTable(CreateTable createTable, ApricotSnapshot snapshot) {
        this.createTable = createTable;
        this.snapshot = snapshot;
    }

    public ApricotTable getTable() {
        return table;
    }

    public List<ForeignKeyIndex> getForeignKeys() {
        return foreignKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsedTable that = (ParsedTable) o;
        return table.equals(that.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table);
    }

    public void parseTable(ParsedBundle parsedBundle) {
        ApricotTable table = new ApricotTable();
        Table cTable = createTable.getTable();
        table.setName(cTable.getName());
        int ordinalPos = 0;
        for (ColumnDefinition cd : createTable.getColumnDefinitions()) {
            ApricotColumn column = new ApricotColumn();
            column.setName(cd.getColumnName());
            column.setDataType(cd.getColDataType().getDataType());
            column.setNullable(!isNotNull(getColumnSpec(cd)));
            column.setValueLength(getArguments(cd));
            column.setOrdinalPosition(ordinalPos);
            ordinalPos++;
            column.setTable(table);
            table.getColumns().add(column);

            //  analyze and create the primary key
            if (isPrimaryKey(getColumnSpec(cd))) {
                buildPrimaryKey(table, column);
            }
        }

        table.setSnapshot(snapshot);
        parseConstraints(createTable, table, parsedBundle);
        this.table = table;
    }

    private void parseConstraints(CreateTable createTable, ApricotTable table,
                                  ParsedBundle parsedBundle) {
        if (createTable.getIndexes() != null) {
            for (Index idx : createTable.getIndexes()) {
                if (idx instanceof ForeignKeyIndex) {
                    foreignKeys.add((ForeignKeyIndex) idx);
                    continue;
                }
                if (idx instanceof NamedConstraint) {
                    SqliteParser.parseNamedConstraint((NamedConstraint) idx, table, parsedBundle);
                }
            }
        }
    }

    /**
     * Create a primary key in case of the "embedded" primary key (included into the main field declaration).
     */
    private void buildPrimaryKey(ApricotTable table, ApricotColumn column) {
        ApricotConstraint pk = new ApricotConstraint();
        pk.setType(ConstraintType.PRIMARY_KEY);
        pk.setName(table.getName() + "_PK");
        pk.setTable(table);
        pk.addColumn(column.getName());
        table.getConstraints().add(pk);
    }

    private String getColumnSpec(ColumnDefinition def) {
        StringBuilder sb = new StringBuilder();
        if (def.getColumnSpecs() != null) {
            for (String s : def.getColumnSpecs()) {
                sb.append(" ").append(s);
            }
        }

        return sb.toString();
    }

    private boolean isNotNull(String spec) {
        if (spec.toLowerCase().contains("not null")) {
            return true;
        }

        return false;
    }

    private boolean isPrimaryKey(String spec) {
        if (spec.toLowerCase().contains("primary key")) {
            return true;
        }

        return false;
    }

    private String getArguments(ColumnDefinition def) {
        StringBuilder sb = new StringBuilder();

        if (def.getColDataType().getArgumentsStringList() != null) {
            boolean first = true;
            for (String s : def.getColDataType().getArgumentsStringList()) {
                if (!first) {
                    sb.append(", ");
                } else {
                    first = false;
                }
                sb.append(s);
            }
        } else {
            return null;
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return table.getName();
    }
}
