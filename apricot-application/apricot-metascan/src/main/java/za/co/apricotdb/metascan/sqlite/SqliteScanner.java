package za.co.apricotdb.metascan.sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.MetaDataScannerBase;
import za.co.apricotdb.metascan.h2.CurrentIndexWrapper;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

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

        // populate indexes and fields
        final CurrentIndexWrapper currentIdx = new CurrentIndexWrapper();
        String idxSql = String.format(
                "select table_name, index_name, column_name, non_unique from information_schema.indexes where is_generated = false and table_schema='%s' order by table_name, index_name, ordinal_position",
                schema);
        jdbc.query(idxSql, (rs, rowNum) -> {
            if (!currentIdx.getIndexName().equals(rs.getString("index_name"))) {
                ApricotTable table = tables.get(rs.getString("table_name"));
                ConstraintType constraintType = null;
                if (rs.getBoolean("non_unique")) {
                    constraintType = ConstraintType.NON_UNIQUE_INDEX;
                } else {
                    constraintType = ConstraintType.UNIQUE_INDEX;
                }

                ApricotConstraint idx = new ApricotConstraint(rs.getString("index_name"), constraintType, table);
                table.getConstraints().add(idx);
                constraints.put(idx.getName(), idx);
                currentIdx.setCurrentIndex(idx);
            }

            currentIdx.getCurrentIndex().addColumn(rs.getString("column_name"));

            return null;
        });

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
                                                      String schema) {
        Map<String, String> indexMap = new HashMap<>();
        String sql = String.format(
                "select unique_index_name, constraint_name, constraint_type from information_schema.constraints where constraint_schema = '%s' and constraint_type = 'PRIMARY KEY' order by unique_index_name",
                schema);
        jdbc.query(sql, (rs, rowNum) -> {
            indexMap.put(rs.getString("unique_index_name"), rs.getString("constraint_name"));

            return null;
        });

        String refSql = String.format(
                "select distinct pk_name, fk_name from information_schema.CROSS_REFERENCES where pktable_schema = '%s' order by pk_name",
                schema);
        List<ApricotRelationship> ret = jdbc.query(refSql, (rs, rowNum) -> {
            String sParent = indexMap.get(rs.getString("pk_name"));
            if (sParent == null) {
                throw new IllegalArgumentException(
                        "Unable to find the parent mapping for the following key=[" + rs.getString("pk_name") + "]");
            }
            String sChild = rs.getString("fk_name");
            ApricotConstraint parent = constraints.get(sParent);
            if (parent == null) {
                throw new IllegalArgumentException(
                        "Unable to find the parent constraint with the name=[" + sParent + "]");
            }
            ApricotConstraint child = constraints.get(sChild);
            if (child == null) {
                throw new IllegalArgumentException(
                        "Unable to find the child constraint with the name=[" + sChild + "]");
            }

            ApricotRelationship r = new ApricotRelationship(parent, child);

            return r;
        });

        return ret;
    }
}
