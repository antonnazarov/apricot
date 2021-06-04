package za.co.apricotdb.metascan.sqlserver;

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
 * Implementation of the scanner for SQL Server.
 *
 * @author Anton Nazarov
 * @since 26/09/2018
 */
@Component
public class SqlServerScanner extends MetaDataScannerBase {

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        String sql = String.format(
                "select table_name from INFORMATION_SCHEMA.tables where table_type='BASE TABLE' and table_schema='%s' order by table_name",
                schema);
        List<ApricotTable> tables = jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable t = new ApricotTable();
            t.setName(rs.getString("table_name"));
            t.setSnapshot(snapshot);

            return t;
        });

        Map<String, ApricotTable> ret = new HashMap<>();
        for (ApricotTable t : tables) {
            ret.put(t.getName(), t);
        }

        return ret;
    }

    @Override
    public Map<String, ApricotColumn> getColumns(JdbcOperations jdbc, Map<String, ApricotTable> tables, String schema) {
        String sql = String.format(
                "select table_name, column_name, ordinal_position, is_nullable, data_type, character_maximum_length from INFORMATION_SCHEMA.COLUMNS where table_schema='%s' order by table_name, ordinal_position",
                schema);
        List<ApricotColumn> columns = jdbc.query(sql, (rs, rowNum) -> {
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

            ApricotTable t = tables.get(rs.getString("table_name"));
            if (t != null) {
                c.setTable(t);
                t.getColumns().add(c);
            }

            return c;
        });

        Map<String, ApricotColumn> ret = new HashMap<>();
        for (ApricotColumn c : columns) {
            ret.put(c.getName(), c);
        }

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
                    break;
                case "FOREIGN KEY":
                    constraintType = ConstraintType.FOREIGN_KEY;
                    break;
                case "UNIQUE":
                    constraintType = ConstraintType.UNIQUE;
                    break;
                }

                if (constraintType != null) {
                    ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                    table.getConstraints().add(c);
                    constraints.put(c.getName(), c);
                }
            }

            return null;
        });

        // populate the newly created constraints with the related fields
        String fSql = String.format(
                "select table_name, constraint_name, ordinal_position, column_name from INFORMATION_SCHEMA.KEY_COLUMN_USAGE where constraint_schema='%s' order by table_name, constraint_name, ordinal_position",
                schema);
        jdbc.query(fSql, (rs, rowNum) -> {
            ApricotConstraint c = constraints.get(rs.getString("constraint_name"));
            if (c != null) {
                String columnName = rs.getString("column_name");
                c.addColumn(columnName);
            }

            return null;
        });

        addIndexes(jdbc, tables, constraints, schema);

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
            String schema) {
        String sql = String.format(
                "select unique_constraint_name, constraint_name from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS where constraint_schema='%s' order by unique_constraint_name",
                schema);
        List<ApricotRelationship> ret = jdbc.query(sql, (rs, rowNum) -> {
            String sParent = rs.getString("unique_constraint_name");
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

    private void addIndexes(JdbcOperations jdbc, Map<String, ApricotTable> tables,
            Map<String, ApricotConstraint> constraints, String schema) {
        jdbc.query(
                "select t.name as table_name, t.object_id, idx.name as index_name, idx.index_id, idx.is_primary_key, idx.is_unique, idx.is_unique_constraint from sys.indexes idx join sys.tables t on t.object_id = idx.object_id where t.type = 'U' and idx.name is not null and idx.is_primary_key = 0 and idx.is_unique_constraint = 0 order by t.name, idx.name",
                (rs, rowNum) -> {
                    String constraintName = rs.getString("index_name");
                    if (constraints.get(constraintName) == null) {
                        ApricotTable table = tables.get(rs.getString("table_name"));
                        if (table != null) {
                            ConstraintType constraintType = null;
                            switch (rs.getInt("is_unique")) {
                            case 0:
                                constraintType = ConstraintType.NON_UNIQUE_INDEX;
                                break;
                            case 1:
                                constraintType = ConstraintType.UNIQUE_INDEX;
                                break;
                            }

                            ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                            table.getConstraints().add(c);

                            // get the fields of the index
                            List<String> columns = jdbc.query(
                                    "select name as column_name from sys.index_columns ic join sys.all_columns c on c.column_id = ic.column_id and c.object_id = ic.object_id where ic.object_id = ? and ic.index_id = ? order by ic.key_ordinal",
                                    new Object[] { rs.getLong("object_id"), rs.getInt("index_id") },
                                    (rs01, rowNum01) -> {
                                        return rs01.getString("column_name");
                                    });

                            for (String col : columns) {
                                c.addColumn(col);
                            }
                            constraints.put(c.getName(), c);
                        }
                    }

                    return null;
                });
    }
}
