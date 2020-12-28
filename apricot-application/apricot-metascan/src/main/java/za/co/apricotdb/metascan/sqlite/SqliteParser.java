package za.co.apricotdb.metascan.sqlite;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.create.table.NamedConstraint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

import java.util.List;

/**
 * This component parses the "CREATE TABLE" meta string into the table attributes.
 *
 * @author Anton Nazarov
 * @since 16/09/2020
 */
@Component
public class SqliteParser {

    public void parseSql(String sql, ParsedBundle parsedBundle, ApricotSnapshot snapshot) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof CreateTable) {
                ParsedTable parsedTable = ParsedTable.parseTable((CreateTable) statement, snapshot, parsedBundle);

                if (parsedTable != null) {
                    parsedBundle.addParsedTable(parsedTable);
                }
            } else if (statement instanceof CreateIndex) {
                parseIndex((CreateIndex) statement, parsedBundle);
            }
        } catch (JSQLParserException ex) {
            throw new IllegalArgumentException("Unable to parse the following SQL expression: " + sql, ex);
        }
    }

    public static void parseNamedConstraint(NamedConstraint nmdConstraint, ApricotTable table,
                                            ParsedBundle parsedBundle) {
        ApricotConstraint constraint = new ApricotConstraint();
        constraint.setTable(table);
        String name = nmdConstraint.getName();
        if (nmdConstraint.getType().equalsIgnoreCase("primary key")) {
            constraint.setType(ConstraintType.PRIMARY_KEY);
            if (StringUtils.isEmpty(name)) {
                name = table.getName() + "_PK";
            }
        } else if (nmdConstraint.getType().equalsIgnoreCase("unique")) {
            constraint.setType(ConstraintType.UNIQUE_INDEX);
            if (StringUtils.isEmpty(name)) {
                name = table.getName() + "_UIDX" + parsedBundle.getNextCounter();
            }
        } else {
            constraint.setType(ConstraintType.NON_UNIQUE_INDEX);
            if (StringUtils.isEmpty(name)) {
                name = table.getName() + "_IDX" + parsedBundle.getNextCounter();
            }
        }
        constraint.setName(name);

        List<String> columns = nmdConstraint.getColumnsNames();
        for (String col : columns) {
            constraint.addColumn(col);
        }

        table.getConstraints().add(constraint);
    }

    private void parseIndex(CreateIndex index, ParsedBundle parsedBundle) {
        String tableName = index.getTable().getName();
        if (parsedBundle.getParsedTable(tableName) != null) {
            ApricotTable table = parsedBundle.getParsedTable(tableName).getTable();
            Index idx = index.getIndex();

            ApricotConstraint constraint = new ApricotConstraint();
            constraint.setTable(table);
            String name = idx.getName();
            if ("unique".equalsIgnoreCase(idx.getType())) {
                constraint.setType(ConstraintType.UNIQUE_INDEX);
                if (StringUtils.isEmpty(name)) {
                    name = table.getName() + "_UIDX" + parsedBundle.getNextCounter();
                }
            } else {
                constraint.setType(ConstraintType.NON_UNIQUE_INDEX);
                if (StringUtils.isEmpty(name)) {
                    name = table.getName() + "_IDX" + parsedBundle.getNextCounter();
                }
            }
            constraint.setName(name);

            for (String col : idx.getColumnsNames()) {
                constraint.addColumn(col);
            }

            table.getConstraints().add(constraint);
        }
    }
}
