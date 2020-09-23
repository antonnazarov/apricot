package za.co.apricotdb.metascan.sqlite;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

import java.util.ArrayList;
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
                ParsedTable parsedTable = ParsedTable.parseTable((CreateTable) statement, snapshot);

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

    private void parseIndex(CreateIndex index, ParsedBundle parsedBundle) {
        String tableName = index.getTable().getName();
        Index idx = index.getIndex();

        System.out.println("table: " + tableName + ", type: " + idx.getType() + ", name: " + idx.getName() + ", " +
                "columns: " + idx.getColumnsNames());
    }
}
