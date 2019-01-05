package za.co.apricotdb.metascan.sqlserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * Implementation of the scanner for SQL Server.
 *
 * @author Anton Nazarov
 * @since 26/09/2018
 */
@Component
public class SqlServerScanner implements MetaDataScanner {

    @Override
    public MetaData scan(String driverClassName, String url, String userName, String password) {
        JdbcOperations jdbc = MetaDataScanner.getTargetJdbcOperations(driverClassName, url, userName, password);

        Map<String, ApricotTable> tables = getTables(jdbc);
        getColumns(jdbc, tables);
        Map<String, ApricotConstraint> constraints = getConstraints(jdbc, tables);
        addIndexes(jdbc, tables, constraints);
        
        List<ApricotRelationship> relationships = getRelationships(jdbc, constraints);
        
        MetaData ret = new MetaData();
        ret.setTables(new ArrayList(tables.values()));
        ret.setRelationships(relationships);

        return ret;
    }

    private Map<String, ApricotTable> getTables(JdbcOperations jdbc) {

        List<ApricotTable> tables = jdbc.query("select table_name "
                + "from INFORMATION_SCHEMA.tables "
                + "where table_type = 'BASE TABLE' "
                + "order by table_name",
                (rs, rowNum) -> {
                    ApricotTable t = new ApricotTable();
                    t.setName(rs.getString("table_name"));

                    return t;
                }
        );

        Map<String, ApricotTable> ret = new HashMap<>();
        for (ApricotTable t : tables) {
            ret.put(t.getName(), t);
        }

        return ret;
    }

    private Map<String, ApricotColumn> getColumns(JdbcOperations jdbc, Map<String, ApricotTable> tables) {
        List<ApricotColumn> columns = jdbc.query("select table_name, column_name, ordinal_position, is_nullable, data_type, character_maximum_length "
                + "from INFORMATION_SCHEMA.COLUMNS "
                + "order by table_name, ordinal_position",
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
                    c.setValueLength(String.valueOf(rs.getInt("character_maximum_length")));

                    ApricotTable t = tables.get(rs.getString("table_name"));
                    if (t != null) {
                        c.setTable(t);
                        t.getColumns().add(c);
                    }

                    return c;
                }
        );

        Map<String, ApricotColumn> ret = new HashMap<>();
        for (ApricotColumn c : columns) {
            ret.put(c.getName(), c);
        }

        return ret;
    }

    private Map<String, ApricotConstraint> getConstraints(JdbcOperations jdbc, Map<String, ApricotTable> tables) {
        List<ApricotConstraint> cns = jdbc.query("select table_name, constraint_type, constraint_name "
                + "from INFORMATION_SCHEMA.TABLE_CONSTRAINTS "
                + "order by table_name, constraint_type, constraint_name",
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

                    return c;
                }
        );

        Map<String, ApricotConstraint> constraints = new HashMap<>();
        for (ApricotConstraint c : cns) {
            constraints.put(c.getName(), c);
        }

        //  populate the newly created constraints with the related fields
        jdbc.query("select table_name, constraint_name, ordinal_position, column_name "
                + "from INFORMATION_SCHEMA.KEY_COLUMN_USAGE "
                + "order by table_name, constraint_name, ordinal_position",
                (rs, rowNum) -> {
                    ApricotConstraint c = constraints.get(rs.getString("constraint_name"));
                    String columnName = rs.getString("column_name");
                    c.addColumn(columnName);

                    return null;
                }
        );

        return constraints;
    }

    private void addIndexes(JdbcOperations jdbc, Map<String, ApricotTable> tables, Map<String, ApricotConstraint> constraints) {

        List<ApricotConstraint> cns = jdbc.query("select t.name as table_name, t.object_id, idx.name as index_name, idx.index_id, idx.is_primary_key, idx.is_unique, idx.is_unique_constraint "
                + "from sys.indexes idx "
                + "join sys.tables t on t.object_id = idx.object_id "
                + "where t.type = 'U' and idx.name is not null and idx.is_primary_key = 0 and idx.is_unique_constraint = 0 "
                + "order by t.name, idx.name",
                (rs, rowNum) -> {
                    String constraintName = rs.getString("index_name");
                    if (constraints.get(constraintName) == null) {
                        ApricotTable table = tables.get(rs.getString("table_name"));
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

                        //  get the fields of the index
                        List<String> columns = jdbc.query(
                                "select name as column_name "
                                + "from sys.index_columns ic "
                                + "join sys.all_columns c on c.column_id = ic.column_id and c.object_id = ic.object_id "
                                + "where ic.object_id = ? and ic.index_id = ? "
                                + "order by ic.key_ordinal",
                                new Object[]{rs.getLong("object_id"), rs.getInt("index_id")},
                                (rs01, rowNum01) -> {
                                    return rs01.getString("column_name");
                                }
                        );

                        for (String col : columns) {
                            c.addColumn(col);
                        }

                        return c;
                    }

                    return null;
                }
        );

        for (ApricotConstraint c : cns) {
            if (c != null) {
                constraints.put(c.getName(), c);
            }
        }
    }

    private List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints) {
        List<ApricotRelationship> ret = jdbc.query(
                "select unique_constraint_name, constraint_name "
                + "from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS "
                + "order by unique_constraint_name",
                (rs, rowNum) -> {
                    String sParent = rs.getString("unique_constraint_name");
                    String sChild = rs.getString("constraint_name");
                    ApricotConstraint parent = constraints.get(sParent);
                    if (parent == null) {
                        throw new IllegalArgumentException("Unable to find the parent exception with the name=[" + sParent + "]");
                    }
                    ApricotConstraint child = constraints.get(sChild);
                    if (parent == null) {
                        throw new IllegalArgumentException("Unable to find the child exception with the name=[" + sChild + "]");
                    }
                    
                    ApricotRelationship r = new ApricotRelationship(parent, child);
                    
                    return r;
                }
        );

        return ret;
    }
}
