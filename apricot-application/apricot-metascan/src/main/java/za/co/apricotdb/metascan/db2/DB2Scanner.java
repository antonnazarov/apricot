package za.co.apricotdb.metascan.db2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;

import za.co.apricotdb.metascan.MetaDataScannerBase;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * This is the DB2 database scanner.
 * 
 * @author Anton Nazarov
 * @since 07/05/2019
 */
public class DB2Scanner extends MetaDataScannerBase {

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        String sql = String.format("select name from SYSIBM.SYSTABLES where CREATOR = '%s' order by name", schema);
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
                "select tbname, name, colno, nulls, coltype, length, scale from SYSIBM.SYSCOLUMNS where TBCREATOR = '%s' order by tbname, colno",
                schema);
        List<ApricotColumn> columns = jdbc.query(sql, (rs, rowNum) -> {
            ApricotColumn c = new ApricotColumn();
            c.setName(rs.getString("name"));
            c.setOrdinalPosition(rs.getInt("colno"));
            if (rs.getString("is_nullable").equals("N")) {
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
                "select tbname, constname, type from SYSIBM.SYSTABCONST where TBCREATOR = '%s' order by tbname",
                schema);
        Map<String, ApricotConstraint> constraints = new HashMap<>();
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable table = tables.get(rs.getString("tbname"));
            if (table != null) {
                String constraintName = rs.getString("constname");
                ConstraintType constraintType = null;
                switch (rs.getString("type")) {
                case "P":
                    constraintType = ConstraintType.PRIMARY_KEY;
                    break;
                case "U":
                    constraintType = ConstraintType.UNIQUE;
                    break;
                }

                ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                table.getConstraints().add(c);
                constraints.put(c.getName(), c);
            }

            return null;
        });

        // constraint columns
        sql = String.format(
                "select tbname, constname, colname, colseq from SYSIBM.SYSKEYCOLUSE where tbcreator = '%s' order by tbname, constname, colseq",
                schema);
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotConstraint c = constraints.get(rs.getString("constname"));
            if (c != null) {
                c.addColumn(rs.getString("colname"));
            }

            return null;
        });

        // indexes
        sql = String.format(
                "select tbname, name, uniquerule from SYSIBM.SYSINDEXES where creator = '%s' order by tbname, name",
                schema);
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable table = tables.get(rs.getString("tbname"));
            if (table != null) {
                String indexnameName = rs.getString("name");
                ConstraintType constraintType = null;
                switch (rs.getString("uniquerule")) {
                case "D":
                    constraintType = ConstraintType.NON_UNIQUE_INDEX;
                    break;
                default:
                    constraintType = ConstraintType.UNIQUE_INDEX;
                    break;
                }

                ApricotConstraint c = new ApricotConstraint(indexnameName, constraintType, table);
                table.getConstraints().add(c);
                constraints.put(c.getName(), c);
            }

            return null;
        });

        // index columns
        sql = String.format(
                "select ixname, colname, colseq from SYSIBM.SYSKEYS where ixcreator = '%s' order by ixname, colseq",
                schema);

        jdbc.query(sql, (rs, rowNum) -> {
            ApricotConstraint c = constraints.get(rs.getString("ixname"));
            if (c != null) {
                c.addColumn(rs.getString("colname"));
            }

            return null;
        });

        // the foreign keys
        Map<ApricotTable, ApricotConstraint> fk = getForeignKeys(jdbc, tables, schema);
        fixForeignKeyNames(fk, constraints);

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
            String schema) {
        String sql = String.format(
                "select tbname, reftbname, relname from SYSIBM.SYSRELS where creator = '%s' and reftbcreator = '%s' order by tbname",
                schema, schema);
        List<ApricotRelationship> ret = jdbc.query(sql, (rs, rowNum) -> {

            ApricotConstraint pk = getPrimaryKey(rs.getString("reftbname"), constraints);
            if (pk == null) {
                throw new IllegalArgumentException(
                        "Unable to find the primary key for the parent table=[" + rs.getString("reftabname") + "]");
            }
            ApricotConstraint fk = getForeignKey(rs.getString("tbname"), rs.getString("relname"), constraints);
            if (fk == null) {
                throw new IllegalArgumentException("Unable to find the foreign key constraint for the child table=["
                        + rs.getString("tbname") + "] and relationship name=[" + rs.getString("relname") + "]");
            }

            return new ApricotRelationship(pk, fk);
        });

        return ret;
    }

    private ApricotConstraint getPrimaryKey(String tableName, Map<String, ApricotConstraint> constraints) {
        ApricotConstraint pk = null;

        for (ApricotConstraint c : constraints.values()) {
            if (c.getType() == ConstraintType.PRIMARY_KEY && c.getTable().getName().equals(tableName)) {
                pk = c;
                break;
            }
        }

        return pk;
    }

    private ApricotConstraint getForeignKey(String tableName, String constraintName,
            Map<String, ApricotConstraint> constraints) {
        ApricotConstraint fk = null;

        for (ApricotConstraint c : constraints.values()) {
            if (c.getType() == ConstraintType.FOREIGN_KEY && c.getTable().getName().equals(tableName)) {
                if (constraintName.equals(c.getName())) {
                    fk = c;
                } else if (constraintName.contains("___") && constraintName.contains(c.getName())) {
                    fk = c;
                }
                break;
            }
        }

        return fk;
    }

    /**
     * Get foreign key constraints per table.
     */
    private Map<ApricotTable, ApricotConstraint> getForeignKeys(JdbcOperations jdbc, Map<String, ApricotTable> tables,
            String schema) {

        // relationship per child table
        Map<ApricotTable, ApricotConstraint> cnstr = new HashMap<>();
        String sql = String.format(
                "select distinct tbname, relname from SYSIBM.SYSFOREIGNKEYS where CREATOR = '%s' order by tbname, relname",
                schema);
        String subSql = String.format(
                "select colname, colseq from SYSIBM.SYSFOREIGNKEYS where CREATOR = '%s' and tbname=? and relname=? order by colseq",
                schema);
        jdbc.query(sql, (rs, rowNum) -> {
            ApricotTable tbl = tables.get(rs.getString("tbname"));
            if (tbl != null) {
                final ApricotConstraint c = new ApricotConstraint(rs.getString("relname"), ConstraintType.FOREIGN_KEY,
                        tbl);
                cnstr.put(tbl, c);

                jdbc.query(subSql, new Object[] { rs.getString("tbname"), rs.getString("relname") }, (rs1, rowNum1) -> {
                    c.addColumn(rs.getString("colname"));

                    return null;
                });
            }
            return null;
        });

        return cnstr;
    }

    /**
     * Handle the foreign key constraint with the same name.
     */
    private void fixForeignKeyNames(Map<ApricotTable, ApricotConstraint> fkConstraints,
            Map<String, ApricotConstraint> constraints) {
        Map<String, Short> register = new HashMap<>();

        for (ApricotConstraint c : fkConstraints.values()) {
            Short cnt = register.get(c.getName());
            if (cnt == null) {
                register.put(c.getName(), (short) 0);
                constraints.put(c.getName(), c);
            } else {
                cnt++;
                register.put(c.getName(), cnt);
                String cNewName = c.getName() + "___" + cnt;
                c.setName(cNewName);
                constraints.put(cNewName, c);
            }
        }
    }
}
