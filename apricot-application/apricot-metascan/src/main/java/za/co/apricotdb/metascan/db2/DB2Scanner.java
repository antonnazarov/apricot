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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
            String schema) {
        // TODO Auto-generated method stub
        return null;
    }

}
