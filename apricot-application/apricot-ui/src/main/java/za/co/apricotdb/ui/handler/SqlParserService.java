package za.co.apricotdb.ui.handler;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper service implementation for use of jSqlParser functionality.
 *
 * @author Anton Nazarov
 * @since 12/08/2020
 */
@Component
public class SqlParserService {

    public List<String> getTables(String sql) throws JSQLParserException {
        List<String> ret = new ArrayList<>();
        Statements stmt = CCJSqlParserUtil.parseStatements(sql);
        for (Statement s : stmt.getStatements()) {
            ret.addAll(getTables(s));
        }

        return ret;
    }

    private List<String> getTables(Statement s) {
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        return tablesNamesFinder.getTableList(s);
    }
}
