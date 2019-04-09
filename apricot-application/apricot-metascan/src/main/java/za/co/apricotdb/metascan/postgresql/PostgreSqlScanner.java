package za.co.apricotdb.metascan.postgresql;

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
 * A scanner of the PostgreSQL database.
 * 
 * @author Anton Nazarov
 * @since 06/04/2019
 */
@Component
public class PostgreSqlScanner extends MetaDataScannerBase {

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        List<ApricotTable> tables = jdbc.query(
                "select table_name from information_schema.tables where table_schema='public' and table_type = 'BASE TABLE' order by table_name;",
                (rs, rowNum) -> {
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
        List<ApricotColumn> columns = jdbc.query(
                "select table_name, column_name, ordinal_position, is_nullable, data_type, character_maximum_length from information_schema.columns where table_schema='public' order by table_name, ordinal_position",
                (rs, rowNum) -> {
                    ApricotColumn c = new ApricotColumn();
                    c.setName(rs.getString("column_name"));
                    c.setOrdinalPosition(rs.getInt("ordinal_position"));
                    if (rs.getString("is_nullable").equals("YES")) {
                        c.setNullable(true);
                    } else {
                        c.setNullable(false);
                    }
                    c.setDataType(rs.getString("data_type"));
                    if (c.getDataType().equals("character varying") || c.getDataType().equals("bit")) {
                        c.setValueLength(rs.getString("character_maximum_length"));
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
        Map<String, ApricotConstraint> constraints = new HashMap<>();
        jdbc.query(
                "select table_name, constraint_name, constraint_type from information_schema.table_constraints where table_schema = 'public' and constraint_type in ('PRIMARY KEY', 'FOREIGN KEY', 'UNIQUE') order by table_name",
                (rs, rowNum) -> {
                    ApricotTable table = tables.get(rs.getString("table_name"));
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

                    ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                    table.getConstraints().add(c);

                    constraints.put(c.getName(), c);

                    return null;
                });

        // handle constraint columns
        jdbc.query(
                "select table_name, constraint_name, column_name, ordinal_position from information_schema.key_column_usage where table_schema='public' order by table_name, constraint_name, ordinal_position",
                (rs, rowNum) -> {
                    ApricotConstraint c = constraints.get(rs.getString("constraint_name"));
                    if (c != null) {
                        c.addColumn(rs.getString("column_name"));
                    }

                    return null;
                });

        // populate indexes and fields
        jdbc.query(
                "select tablename, indexname, indexdef from pg_catalog.pg_indexes where schemaname='public' order by tablename",
                (rs, rowNum) -> {
                    if (constraints.get(rs.getString("indexname")) == null) {
                        String indexdef = rs.getString("indexdef");
                        ApricotTable table = tables.get(rs.getString("tablename"));
                        ConstraintType constraintType = null;
                        if (indexdef.contains("CREATE UNIQUE INDEX")) {
                            constraintType = ConstraintType.UNIQUE_INDEX;
                        } else {
                            constraintType = ConstraintType.NON_UNIQUE_INDEX;
                        }

                        ApricotConstraint idx = new ApricotConstraint(rs.getString("indexname"), constraintType, table);
                        table.getConstraints().add(idx);
                        constraints.put(idx.getName(), idx);

                        int startIdx = indexdef.indexOf("(");
                        int endIdx = indexdef.indexOf(")");
                        if (startIdx != -1 && endIdx != -1) {
                            String sCols = indexdef.substring(startIdx + 1, endIdx);
                            String[] cols = sCols.split(",");
                            for (String column : cols) {
                                idx.addColumn(column.trim());
                            }
                        }
                    }
                    return null;
                });

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
            String schema) {
        List<ApricotRelationship> ret = jdbc.query(
                "select unique_constraint_name, constraint_name from information_schema.referential_constraints where constraint_schema='public' order by unique_constraint_name",
                (rs, rowNum) -> {
                    String sParent = rs.getString("unique_constraint_name");
                    ApricotConstraint parent = constraints.get(sParent);
                    if (parent == null) {
                        throw new IllegalArgumentException(
                                "Unable to find the parent constraint with the name=[" + sParent + "]");
                    }
                    String sChild = rs.getString("constraint_name");
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
