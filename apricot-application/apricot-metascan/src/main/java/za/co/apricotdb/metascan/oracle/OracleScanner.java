package za.co.apricotdb.metascan.oracle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Scanner for the Oracle database.
 * 
 * @author Anton Nazarov
 * @since 04/10/2018, 05/04/2019
 */
@Component
public class OracleScanner extends MetaDataScannerBase {

    Logger logger = LoggerFactory.getLogger(OracleScanner.class);

    @Override
    public Map<String, ApricotTable> getTables(JdbcOperations jdbc, ApricotSnapshot snapshot, String schema) {
        List<ApricotTable> tables = jdbc.query("select table_name from user_tables order by table_name",
                (rs, rowNum) -> {
                    ApricotTable t = new ApricotTable();
                    t.setName(rs.getString("table_name"));
                    t.setSnapshot(snapshot);

                    return t;
                });

        logger.info("The following tables have been eligible for the scanning: " + tables);

        Map<String, ApricotTable> ret = new HashMap<>();
        for (ApricotTable t : tables) {
            ret.put(t.getName(), t);
        }

        return ret;
    }

    @Override
    public Map<String, ApricotColumn> getColumns(JdbcOperations jdbc, Map<String, ApricotTable> tables, String schema) {
        List<ApricotColumn> columns = jdbc.query(
                "select table_name, column_name, column_id, nullable, data_type, data_length, data_precision from user_tab_columns order by table_name, column_id",
                (rs, rowNum) -> {
                    ApricotColumn c = new ApricotColumn();
                    c.setName(rs.getString("column_name"));
                    c.setOrdinalPosition(rs.getInt("column_id"));
                    if (rs.getString("nullable").equals("Y")) {
                        c.setNullable(true);
                    } else {
                        c.setNullable(false);
                    }
                    c.setDataType(rs.getString("data_type"));
                    if (c.getDataType().equals("VARCHAR2")) {
                        c.setValueLength(rs.getString("data_length"));
                    } else if (c.getDataType().equals("NUMBER")) {
                        c.setValueLength(rs.getString("data_precision"));
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
        List<ApricotConstraint> cns = new ArrayList<>();
        jdbc.query(
                "select constraint_name, constraint_type, table_name, r_constraint_name from user_constraints where constraint_type <> 'C' order by table_name, constraint_type",
                (rs, rowNum) -> {
                    ApricotTable table = tables.get(rs.getString("table_name"));
                    if (table != null) {
                        String constraintName = rs.getString("constraint_name");
                        ConstraintType constraintType = null;
                        switch (rs.getString("constraint_type")) {
                        case "P":
                            constraintType = ConstraintType.PRIMARY_KEY;
                            break;
                        case "R":
                            constraintType = ConstraintType.FOREIGN_KEY;
                            break;
                        case "U":
                            constraintType = ConstraintType.UNIQUE;
                            break;
                        }

                        ApricotConstraint c = new ApricotConstraint(constraintName, constraintType, table);
                        table.getConstraints().add(c);

                        cns.add(c);
                    } else {
                        logger.info("getConstraints: the table [" + rs.getString("table_name") + "] was not found");
                    }

                    return null;
                });

        Map<String, ApricotConstraint> constraints = new HashMap<>();
        for (ApricotConstraint c : cns) {
            constraints.put(c.getName(), c);
        }

        // populate the newly created constraints with the related fields
        jdbc.query(
                "select table_name, constraint_name, column_name, position from user_cons_columns where position is not null order by table_name, constraint_name, position",
                (rs, rowNum) -> {
                    ApricotConstraint c = constraints.get(rs.getString("constraint_name"));
                    if (c != null) {
                        String columnName = rs.getString("column_name");
                        c.addColumn(columnName);
                    }

                    return null;
                });

        // populate indexes
        jdbc.query("select index_name, table_name, uniqueness from user_indexes order by table_name", (rs, rowNum) -> {
            ApricotConstraint c = constraints.get(rs.getString("index_name"));
            if (c == null) {
                ApricotTable table = tables.get(rs.getString("table_name"));
                ConstraintType constraintType = null;
                switch (rs.getString("uniqueness")) {
                case "UNIQUE":
                    constraintType = ConstraintType.UNIQUE_INDEX;
                    break;
                default:
                    constraintType = ConstraintType.NON_UNIQUE_INDEX;
                    break;
                }

                ApricotConstraint idx = new ApricotConstraint(rs.getString("index_name"), constraintType, table);
                table.getConstraints().add(idx);
                constraints.put(idx.getName(), idx);
            }

            return null;
        });

        // populate index fields
        jdbc.query(
                "select table_name, index_name, column_name, column_position from user_ind_columns order by table_name, index_name, column_position",
                (rs, rowNum) -> {
                    ApricotConstraint c = constraints.get(rs.getString("index_name"));
                    if (c != null && (c.getType() == ConstraintType.UNIQUE_INDEX
                            || c.getType() == ConstraintType.NON_UNIQUE_INDEX)) {
                        String columnName = rs.getString("column_name");
                        c.addColumn(columnName);
                    }

                    return null;
                });

        return constraints;
    }

    @Override
    public List<ApricotRelationship> getRelationships(JdbcOperations jdbc, Map<String, ApricotConstraint> constraints,
            String schema) {

        logger.info("Scanning the relationships, the following constraints were identified: "
                + getConstraintsAsString(constraints));
        List<ApricotRelationship> ret = new ArrayList<>();
        jdbc.query(
                "select constraint_name, constraint_type, table_name, r_constraint_name from user_constraints where constraint_type <> 'C' and r_constraint_name is not null order by table_name, constraint_type",
                (rs, rowNum) -> {
                    String sParent = rs.getString("r_constraint_name");
                    String sChild = rs.getString("constraint_name");
                    ApricotConstraint parent = constraints.get(sParent);
                    if (parent == null) {
                        logger.info("The Relationship won't be created: [" + sParent + "->" + sChild
                                + "], the parent constraint was not found");
                        return null;
                    }
                    ApricotConstraint child = constraints.get(sChild);
                    if (child == null) {
                        logger.info("The Relationship won't be created: [" + sParent + "->" + sChild
                                + "], the child constraint was not found");
                        return null;
                    }

                    ApricotRelationship r = new ApricotRelationship(parent, child);
                    ret.add(r);

                    return null;
                });

        return ret;
    }

    private String getConstraintsAsString(Map<String, ApricotConstraint> constraints) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String c : constraints.keySet()) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(c);
        }

        return sb.toString();
    }
}
