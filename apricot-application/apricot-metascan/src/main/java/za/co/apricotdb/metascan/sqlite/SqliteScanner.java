package za.co.apricotdb.metascan.sqlite;

import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.MetaDataScannerBase;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotDatabaseView;
import za.co.apricotdb.persistence.entity.ApricotDatabaseViewColumn;
import za.co.apricotdb.persistence.entity.ApricotDatabaseViewRelatedTable;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scanner for the SQLite database.
 *
 * @author Anton Nazarov
 * @since 08/09/2020
 */
@Component
public class SqliteScanner extends MetaDataScannerBase {

    @Autowired
    SqliteParser tableParser;

    private ParsedBundle parsedBundle;

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        parsedBundle = new ParsedBundle();

        String sql = "select tbl_name, sql from sqlite_master where type = 'table'";
        //  scan through the sql's of type table and populate the ParsedBundle
        jdbc.query(sql, (rs, rowNum) -> {
            tableParser.parseSql(rs.getString("sql"), parsedBundle, snapshot);

            return null;
        });

        return parsedBundle.getScannedApricotTables();
    }

    /**
     * The columns of the tables already have been read in the getTables() method.
     */
    @Override
    public Map<String, ApricotColumn> getColumns(JdbcOperations jdbc, Map<String, ApricotTable> tables, String schema) {
        return null;
    }

    @Override
    public Map<String, ApricotConstraint> getConstraints(JdbcOperations jdbc, Map<String, ApricotTable> tables,
                                                         String schema) {
        Map<String, ApricotConstraint> constraints = new HashMap<>();
        String sql = "select name, sql from sqlite_master where type = 'index' and name not like 'sqlite_autoindex_%'";
        jdbc.query(sql, (rs, rowNum) -> {
            tableParser.parseSql(rs.getString("sql"), parsedBundle, null);

            return null;
        });

        for (ParsedTable pt : parsedBundle.getParsedTables()) {
            for (ApricotConstraint constraint : pt.getTable().getConstraints()) {
                constraints.put(constraint.getName(), constraint);
            }
        }

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
                                                      String schema) {
        List<ApricotRelationship> ret = new ArrayList<>();

        for (ParsedTable pt : parsedBundle.getParsedTables()) {
            if (pt.getForeignKeys() != null) {
                ApricotTable table = pt.getTable();
                for (ForeignKeyIndex fk : pt.getForeignKeys()) {
                    String refTableName = fk.getTable().getName();
                    ParsedTable refTable = parsedBundle.getParsedTable(refTableName);
                    ApricotTable ref = refTable.getTable();
                    //  scan to find the primary key
                    ApricotConstraint parent = null;
                    for (ApricotConstraint c : ref.getConstraints()) {
                        if (c.getType() == ConstraintType.PRIMARY_KEY) {
                            parent = c;
                            break;
                        }
                    }
                    if (parent == null) {
                        continue;
                    }

                    //  create a foreign key constraint
                    ApricotConstraint child = new ApricotConstraint();
                    child.setType(ConstraintType.FOREIGN_KEY);
                    child.setTable(table);
                    if (StringUtils.isNotEmpty(fk.getName())) {
                        child.setName(fk.getName());
                    } else {
                        child.setName(table.getName() + "_FK" + parsedBundle.getNextCounter());
                    }

                    for (String field : fk.getColumnsNames()) {
                        child.addColumn(field);
                    }

                    table.getConstraints().add(child);

                    ApricotRelationship r = new ApricotRelationship(parent, child);
                    ret.add(r);
                }
            }
        }

        return ret;
    }

    @Override
    public Map<String, ApricotDatabaseView> getDatabaseViews(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        return null;
    }

    @Override
    public List<ApricotDatabaseViewColumn> getDatabaseViewColumns(JdbcOperations jdbc, Map<String, ApricotDatabaseView> databaseViews, String schema) {
        return null;
    }

    @Override
    public List<ApricotDatabaseViewRelatedTable> getDatabaseViewRelatedTables(JdbcOperations jdbc, Map<String, ApricotDatabaseView> databaseViews, String schema) {
        return null;
    }
}
