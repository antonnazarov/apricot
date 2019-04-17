package za.co.apricotdb.metascan.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.MetaDataScannerBase;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * Scanner for the MySql database.
 * 
 * @author Anton Nazarov
 * @since 04/10/2018
 */
@Component
public class MySqlScanner extends MetaDataScannerBase {

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        Map<String, ApricotTable> ret = new HashMap<>();

        String sql = String.format(
                "select table_name from information_schema.tables where table_schema='%s' and table_type = 'BASE TABLE' order by table_name",
                schema);
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable t = new ApricotTable();
            t.setName(rs.getString("table_name"));
            t.setSnapshot(snapshot);

            ret.put(t.getName(), t);

            return null;
        });

        return ret;
    }

    @Override
    public Map<String, ApricotColumn> getColumns(JdbcOperations jdbc, Map<String, ApricotTable> tables, String schema) {
        Map<String, ApricotColumn> ret = new HashMap<>();

        String sql = String.format(
                "select table_name, column_name, ordinal_position, is_nullable, data_type, character_maximum_length from INFORMATION_SCHEMA.COLUMNS where table_schema='%s' order by table_name, ordinal_position",
                schema);

        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable t = tables.get(rs.getString("table_name"));
            if (t != null) {
                ApricotColumn c = new ApricotColumn();
                c.setName(rs.getString("column_name"));
                c.setOrdinalPosition(rs.getInt("ordinal_position"));
                if (rs.getString("is_nullable").equals("YES")) {
                    c.setNullable(true);
                } else {
                    c.setNullable(false);
                }
                c.setDataType(rs.getString("data_type"));
                if (rs.getInt("character_maximum_length") != 0) {
                    if (rs.getInt("character_maximum_length") == -1) {
                        // exclusion: length=-1 with any type has to be migrated as "max"
                        c.setValueLength("max");
                    } else if (c.getDataType().equalsIgnoreCase("text")) {
                        // ignore any Value Length if type is "text"
                        c.setValueLength(null);
                    } else {
                        c.setValueLength(String.valueOf(rs.getInt("character_maximum_length")));
                    }
                }
                c.setTable(t);
                t.getColumns().add(c);
                ret.put(c.getName(), c);
            }

            return null;
        });

        return ret;
    }

    @Override
    public Map<String, ApricotConstraint> getConstraints(JdbcOperations jdbc, Map<String, ApricotTable> tables,
            String schema) {
        String sql = String.format(
                "select table_name, constraint_type, constraint_name from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where constraint_schema='%s' order by table_name, constraint_type, constraint_name",
                schema);
        Map<String, ApricotConstraint> constraints = new HashMap<>();

        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable table = tables.get(rs.getString("table_name"));
            if (table != null) {
                String constraintName = rs.getString("constraint_name");
                ConstraintType constraintType = null;
                switch (rs.getString("constraint_type")) {
                case "PRIMARY KEY":
                    constraintType = ConstraintType.PRIMARY_KEY;
                    constraintName = substituteConstraintName(constraintName, table.getName());
                    break;
                case "FOREIGN KEY":
                    constraintType = ConstraintType.FOREIGN_KEY;
                    break;
                case "UNIQUE":
                    constraintType = ConstraintType.UNIQUE;
                    break;
                }

                ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                table.getConstraints().add(c);
                constraints.put(c.getName(), c);
            }

            return null;
        });

        // populate the newly created constraints with the related fields
        String fSql = String.format(
                "select table_name, constraint_name, ordinal_position, column_name from INFORMATION_SCHEMA.KEY_COLUMN_USAGE where constraint_schema='%s' order by table_name, constraint_name, ordinal_position",
                schema);
        jdbc.query(fSql, (rs, rowNum) -> {
            String constraintName = substituteConstraintName(rs.getString("constraint_name"),
                    rs.getString("table_name"));
            ApricotConstraint c = constraints.get(constraintName);
            if (c != null) {
                String columnName = rs.getString("column_name");
                c.addColumn(columnName);
            }

            return null;
        });

        // add indexes
        String idxSql = String.format(
                "select distinct table_name, index_name, non_unique from INFORMATION_SCHEMA.STATISTICS where table_schema = '%s' and index_name <> 'PRIMARY' order by table_name",
                schema);
        String idxCols = String.format(
                "select column_name from INFORMATION_SCHEMA.STATISTICS where table_schema = '%s' and table_name = ? and index_name = ? order by seq_in_index",
                schema);

        jdbc.query(idxSql, (rs, rowNum) -> {
            ApricotTable table = tables.get(rs.getString("table_name"));
            if (table != null && !constraints.containsKey(rs.getString("index_name"))) {
                ConstraintType constraintType = null;
                if (rs.getInt("non_unique") == 1) {
                    constraintType = ConstraintType.NON_UNIQUE_INDEX;
                } else {
                    constraintType = ConstraintType.UNIQUE_INDEX;
                }
                ApricotConstraint c = new ApricotConstraint(rs.getString("index_name"), constraintType, table);
                table.getConstraints().add(c);
                constraints.put(c.getName(), c);

                jdbc.query(idxCols, new String[] { rs.getString("table_name"), rs.getString("index_name") },
                        (rsCol, rowNumCol) -> {
                            c.addColumn(rsCol.getString("column_name"));

                            return null;
                        });
            }

            return null;
        });

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
            String schema) {
        String sql = String.format(
                "select unique_constraint_name, constraint_name, referenced_table_name from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where constraint_schema='%s' order by unique_constraint_name",
                schema);
        List<ApricotRelationship> ret = jdbc.query(sql, (rs, rowNum) -> {
            String sParent = rs.getString("unique_constraint_name");
            sParent = substituteConstraintName(sParent, rs.getString("referenced_table_name"));
            String sChild = rs.getString("constraint_name");
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

    private String substituteConstraintName(String constraintName, String tableName) {
        if (constraintName.equals("PRIMARY")) {
            return tableName + "_PK";
        }
        return constraintName;
    }
}
