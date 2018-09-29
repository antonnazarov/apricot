package za.co.apricotdb.metascan.sqlserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.StructureScanned;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
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
    public StructureScanned scan(String driverClassName, String url, String userName, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Map<String, ApricotTable> getTables(JdbcOperations jdbc) {

        List<ApricotTable> tables = jdbc.query("select table_name \n"
                + "from INFORMATION_SCHEMA.tables\n"
                + "where table_type = 'BASE TABLE'\n"
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
        List<ApricotColumn> columns = jdbc.query("select table_name, column_name, ordinal_position, is_nullable, data_type, character_maximum_length\n"
                + "from Intermediary_Account.INFORMATION_SCHEMA.COLUMNS\n"
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

    private void getConstraints(JdbcOperations jdbc, Map<String, ApricotTable> tables, Map<String, ApricotColumn> columns) {
        List<ApricotConstraint> constraints = jdbc.query("select table_name, constraint_type, constraint_name\n"
                + "from INFORMATION_SCHEMA.TABLE_CONSTRAINTS\n"
                + "order by table_name, constraint_type, constraint_name",
                (rs, rowNum) -> {
                    
                    ApricotTable table = tables.get(rs.getString("table_name"));
                    String constraintName = rs.getString("constraint_name");
                    ConstraintType constraintType = null;
                    switch (rs.getString("constraint_type")) {
                        case "PRIMARY KEY" :
                            constraintType = ConstraintType.PRIMARY_KEY;
                            break;
                        case "FOREIGN KEY" :
                            constraintType = ConstraintType.FOREIGN_KEY;
                            break;
                        case "UNIQUE" :
                            constraintType = ConstraintType.UNIQUE;
                            break;
                    }
                    
                    ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                    
                    return c;
                }
        );

    }
}
