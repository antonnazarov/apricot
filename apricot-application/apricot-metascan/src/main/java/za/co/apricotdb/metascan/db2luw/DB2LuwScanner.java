package za.co.apricotdb.metascan.db2luw;

import org.apache.commons.lang3.RandomStringUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the DB2 LUW database scanner.
 * 
 * @author Anton Nazarov
 * @since 13/01/2020
 */
@Component
public class DB2LuwScanner extends MetaDataScannerBase {

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        String sql = String.format("select name from SYSIBM.SYSTABLES where UPPER(CREATOR) = UPPER('%s') order by name",
                schema);
        List<ApricotTable> tables = jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable t = new ApricotTable();
            t.setName(rs.getString("name"));
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
                "select tbname, name, colno, nulls, coltype, length, scale from SYSIBM.SYSCOLUMNS where UPPER(TBCREATOR) = UPPER('%s') order by tbname, colno",
                schema);
        List<ApricotColumn> columns = jdbc.query(sql, (rs, rowNum) -> {
            ApricotColumn c = new ApricotColumn();
            c.setName(rs.getString("name"));
            c.setOrdinalPosition(rs.getInt("colno"));
            if (rs.getString("nulls").equals("N")) {
                c.setNullable(false);
            } else {
                c.setNullable(true);
            }
            c.setDataType(rs.getString("coltype"));
            int length = rs.getInt("length");
            int scale = rs.getInt("scale");
            if (length != 0 && scale != 0) {
                c.setValueLength(String.valueOf(length) + "," + String.valueOf(scale));
            } else if (length != 0) {
                c.setValueLength(String.valueOf(length));
            }

            ApricotTable t = tables.get(rs.getString("tbname"));
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
                "select tbname, name, constraintyp from SYSIBM.SYSTABCONST where UPPER(TBCREATOR) = UPPER('%s') order by tbname",
                schema);
        Map<String, ApricotConstraint> constraints = new HashMap<>();
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable table = tables.get(rs.getString("tbname"));
            if (table != null) {
                String constraintName = rs.getString("name");
                ConstraintType constraintType = null;
                switch (rs.getString("constraintyp")) {
                case "P":
                    constraintType = ConstraintType.PRIMARY_KEY;
                    break;
                case "U":
                    constraintType = ConstraintType.UNIQUE;
                    break;
                case "F":
                    constraintType = ConstraintType.FOREIGN_KEY;
                    break;
                }

                if (constraintType != null) {
                    ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                    if (addConstraintToTable(table, c)) {
                        addConstraintToMap(c, constraints);
                    }
                }
            }

            return null;
        });

        // constraint columns
        sql = String.format(
                "select tbname, constname, colname, colseq from SYSIBM.SYSKEYCOLUSE where UPPER(tbcreator) = UPPER('%s') order by tbname, constname, colseq",
                schema);
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable table = tables.get(rs.getString("tbname"));
            if (table != null) {
                ApricotConstraint c = getConstraintByName(rs.getString("constname"), constraints, table);
                if (c != null) {
                    c.addColumn(rs.getString("colname"));
                }
            }

            return null;
        });

        // indexes
        sql = String.format(
                "select tbname, name, uniquerule from SYSIBM.SYSINDEXES where UPPER(creator) = UPPER('%s') order by tbname, name",
                schema);
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable table = tables.get(rs.getString("tbname"));
            if (table != null) {
                String indexnameName = rs.getString("name");
                ConstraintType constraintType = null;
                switch (rs.getString("uniquerule")) {
                case "U":
                    constraintType = ConstraintType.UNIQUE_INDEX;
                    break;
                case "D":
                    constraintType = ConstraintType.NON_UNIQUE_INDEX;
                    break;
                default:
                    constraintType = null;
                    break;
                }

                if (constraintType != null) {
                    ApricotConstraint c = new ApricotConstraint(indexnameName, constraintType, table);
                    if (addConstraintToTable(table, c)) {
                        addConstraintToMap(c, constraints);
                    }
                }
            }

            return null;
        });

        // index columns
        sql = String.format(
                "select indname, colname, colseq from SYSIBM.SYSINDEXCOLUSE where UPPER(indschema)=UPPER('%s') order by indname, colseq",
                schema);

        jdbc.query(sql, (rs, rowNum) -> {
            ApricotConstraint c = constraints.get(rs.getString("indname"));
            if (c != null) {
                c.addColumn(rs.getString("colname"));
            }

            return null;
        });

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
            String schema) {
        Map<String, ApricotTable> tables = getConstraintTables(constraints);

        String sql = String.format(
                "select tbname, reftbname, relname from SYSIBM.SYSRELS where UPPER(creator) = UPPER('%s') and UPPER(reftbcreator) = UPPER('%s') order by tbname",
                schema, schema);
        List<ApricotRelationship> ret = jdbc.query(sql, (rs, rowNum) -> {
            ApricotConstraint pk = null;
            ApricotTable table = tables.get(rs.getString("reftbname"));
            if (table != null) {
                pk = getPrimaryKey(table);
                if (pk == null) {
                    throw new IllegalArgumentException(
                            "Unable to find the primary key for the parent table=[" + rs.getString("reftbname") + "]");
                }
            } else {
                throw new IllegalArgumentException("Unable to find table=[" + rs.getString("reftbname")
                        + "], which is a part of the relationship");
            }

            ApricotConstraint fk = null;
            table = tables.get(rs.getString("tbname"));
            if (table != null) {
                fk = getConstraintByName(rs.getString("relname"), constraints, table);
                if (fk == null) {
                    throw new IllegalArgumentException("Unable to find the foreign key constraint for the child table=["
                            + rs.getString("tbname") + "] and relationship name=[" + rs.getString("relname") + "]");
                }
            } else {
                throw new IllegalArgumentException("Unable to find the child table=[" + rs.getString("tbname")
                        + "] for the foreign constraint=[" + rs.getString("relname") + "]");
            }

            return new ApricotRelationship(pk, fk);
        });

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

    private Map<String, ApricotTable> getConstraintTables(Map<String, ApricotConstraint> constraints) {
        Map<String, ApricotTable> ret = new HashMap<>();

        for (ApricotConstraint c : constraints.values()) {
            if (!ret.containsKey(c.getTable().getName())) {
                ret.put(c.getTable().getName(), c.getTable());
            }
        }

        return ret;
    }

    private ApricotConstraint getPrimaryKey(ApricotTable table) {
        ApricotConstraint pk = getConstraintOfType(table, ConstraintType.PRIMARY_KEY);
        if (pk == null) {
            pk = getConstraintOfType(table, ConstraintType.UNIQUE);
        }
        if (pk == null) {
            pk = getConstraintOfType(table, ConstraintType.UNIQUE_INDEX);
        }

        return pk;
    }

    private ApricotConstraint getConstraintOfType(ApricotTable table, ConstraintType type) {
        ApricotConstraint ret = null;

        for (ApricotConstraint c : table.getConstraints()) {
            if (c.getType() == type) {
                ret = c;
                break;
            }
        }

        return ret;
    }

    /**
     * This method adds a constraint to the constraint map resolving the possible
     * constraint name conflicts.
     */
    private void addConstraintToMap(ApricotConstraint constraint, Map<String, ApricotConstraint> constraints) {
        String keyName = constraint.getName();
        if (constraints.containsKey(keyName)) {
            keyName = constraint.getName() + "___" + RandomStringUtils.randomAlphanumeric(5);
        }

        constraints.put(keyName, constraint);
    }

    private boolean addConstraintToTable(ApricotTable table, ApricotConstraint constraint) {
        for (ApricotConstraint c : table.getConstraints()) {
            if (c.equals(constraint)) {
                return false;
            }
        }

        table.getConstraints().add(constraint);
        return true;
    }

    /**
     * Get a constraint by the provided constraint name and its hosting table.
     */
    private ApricotConstraint getConstraintByName(String constraintName, Map<String, ApricotConstraint> constraints,
            ApricotTable table) {
        for (ApricotConstraint c : constraints.values()) {
            if (c.getTable().equals(table) && c.getName().equals(constraintName)) {
                return c;
            }
        }

        return null;
    }
}
