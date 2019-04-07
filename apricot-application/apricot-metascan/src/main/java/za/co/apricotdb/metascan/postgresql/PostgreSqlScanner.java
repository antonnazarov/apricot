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

/**
 * A scanner of the PostgreSQL database.
 * 
 * @author Anton Nazarov
 * @since 06/04/2019
 */
@Component
public class PostgreSqlScanner extends MetaDataScannerBase {

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot) {
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
    public Map<String, ApricotColumn> getColumns(JdbcOperations jdbc, Map<String, ApricotTable> tables) {
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
                    c.setDataType(rs.getString("type_name"));
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
    public Map<String, ApricotConstraint> getConstraints(JdbcOperations jdbc, Map<String, ApricotTable> tables) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints) {
        // TODO Auto-generated method stub
        return null;
    }
}
