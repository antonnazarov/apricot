package za.co.apricotdb.metascan.sqlite;

import za.co.apricotdb.persistence.entity.ApricotTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The storage of the parsed tables collection.
 *
 * @author Anton Nazarov
 * @since 23/09/2020
 */
public class ParsedBundle {

    private Map<String, ParsedTable> parsedTables = new HashMap<>();

    public List<ParsedTable> getParsedTables() {
        return new ArrayList<>(parsedTables.values());
    }

    public ParsedTable getParsedTable(String tableName) {
        return parsedTables.get(tableName);
    }

    public Map<String, ApricotTable> getScannedApricotTables() {
        Map<String, ApricotTable> ret = new HashMap<>();
        for (ParsedTable pt : getParsedTables()) {
            ret.put(pt.toString(), pt.getTable());
        }

        return ret;
    }

    public void addParsedTable(ParsedTable parsedTable) {
        parsedTables.put(parsedTable.toString(), parsedTable);
    }
}
