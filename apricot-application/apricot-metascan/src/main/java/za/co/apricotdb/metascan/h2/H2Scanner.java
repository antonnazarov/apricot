package za.co.apricotdb.metascan.h2;

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
 * Scanner for the H2 database.
 * 
 * @author Anton Nazarov
 * @since 04/10/2018
 */
@Component
public class H2Scanner extends MetaDataScannerBase {

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        List<ApricotTable> tables = jdbc.query(
                "select table_name from information_schema.tables where table_schema='PUBLIC' and table_type='TABLE' order by table_name",
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
                "select table_name, column_name, ordinal_position, is_nullable, type_name, character_maximum_length from information_schema.columns where table_schema='PUBLIC' order by table_name, ordinal_position",
                (rs, rowNum) -> {
                    ApricotColumn c = new ApricotColumn();
                    c.setName(rs.getString("column_name"));
                    c.setOrdinalPosition(rs.getInt("ordinal_position"));
                    if (rs.getString("is_nullable").equals("YES")) {
                        c.setNullable(true);
                    } else {
                        c.setNullable(false);
                    }
                    c.setDataType(rs.getString("type_name"));
                    if (c.getDataType().equals("VARCHAR2")) {
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
                "select table_name, constraint_name, constraint_type, column_list from information_schema.constraints where table_schema='PUBLIC' and constraint_schema = 'PUBLIC' order by table_name",
                (rs, rowNum) -> {
                    ApricotTable table = tables.get(rs.getString("table_name"));
                    String constraintName = rs.getString("constraint_name");
                    ConstraintType constraintType = null;
                    switch (rs.getString("constraint_type")) {
                    case "PRIMARY_KEY":
                        constraintType = ConstraintType.PRIMARY_KEY;
                        break;
                    case "REFERENTIAL":
                        constraintType = ConstraintType.FOREIGN_KEY;
                        break;
                    case "UNIQUE":
                        constraintType = ConstraintType.UNIQUE;
                        break;
                    }

                    ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                    table.getConstraints().add(c);

                    // handle the constraint columns
                    String[] cols = rs.getString("column_list").split(",");
                    for (String col : cols) {
                        c.addColumn(col);
                    }

                    constraints.put(c.getName(), c);

                    return null;
                });

        // populate indexes and fields
        final CurrentIndexWrapper currentIdx = new CurrentIndexWrapper();
        jdbc.query(
                "select table_name, index_name, column_name, non_unique from information_schema.indexes where is_generated = false and table_schema='PUBLIC' order by table_name, index_name, ordinal_position",
                (rs, rowNum) -> {
                    if (!currentIdx.getIndexName().equals(rs.getString("index_name"))) {
                        ApricotTable table = tables.get(rs.getString("table_name"));
                        ConstraintType constraintType = null;
                        if (rs.getBoolean("non_unique")) {
                            constraintType = ConstraintType.NON_UNIQUE_INDEX;
                        } else {
                            constraintType = ConstraintType.UNIQUE_INDEX;
                        }

                        ApricotConstraint idx = new ApricotConstraint(rs.getString("index_name"), constraintType,
                                table);
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
        jdbc.query(
                "select unique_index_name, constraint_name, constraint_type from information_schema.constraints where table_schema='PUBLIC' and constraint_schema = 'PUBLIC' and constraint_type = 'PRIMARY_KEY' order by unique_index_name",
                (rs, rowNum) -> {
                    indexMap.put(rs.getString("unique_index_name"), rs.getString("constraint_name"));

                    return null;
                });

        List<ApricotRelationship> ret = jdbc.query(
                "select distinct pk_name, fk_name from information_schema.CROSS_REFERENCES where pktable_schema = 'PUBLIC' order by pk_name",
                (rs, rowNum) -> {
                    String sParent = indexMap.get(rs.getString("pk_name"));
                    if (sParent == null) {
                        throw new IllegalArgumentException("Unable to find the parent mapping for the following key=["
                                + rs.getString("pk_name") + "]");
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
