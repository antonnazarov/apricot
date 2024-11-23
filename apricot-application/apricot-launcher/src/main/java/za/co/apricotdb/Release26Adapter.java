package za.co.apricotdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Adaptor for Release 2.6.
 *
 * @author Anton Nazarov
 * @since 17/06/2021
 */
public class Release26Adapter implements DatabaseStructureAdapter {

    @Override
    public boolean adapt(Connection connection) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            boolean addDb2Luw = false;
            boolean mariaDb = false;

            statement = connection.prepareStatement("select app_parameter_name, app_parameter_value from apricot_app_parameter where app_parameter_name in (?,?)");
            statement.setString(1, "DB2 (LUW).datatypes");
            statement.setString(2, "MariaDB.datatypes");
            rs = statement.executeQuery();
            while (rs.next()) {
                String value = rs.getString("app_parameter_name");
                if ("DB2 (LUW).datatypes".equals(value)) {
                    addDb2Luw = true;
                }
                if ("MariaDB.datatypes".equals(value)) {
                    mariaDb = true;
                }
            }
            rs.close();

            if (!addDb2Luw) {
                insertDbParamValue("DB2 (LUW).datatypes", "BIGINT;SMALLINT;INTEGER;DOUBLE;NUMERIC;DATE;REAL;TIME;TIMESTAMP;CHAR;VARCHAR;LONG VARCHAR;CLOB;BLOB", connection);
            }
            if (!mariaDb) {
                insertDbParamValue("MariaDB.datatypes", "CHAR;VARCHAR;BINARY;VARBINARY;BLOB;TEXT;ENUM;SET;INTEGER;SMALLINT;DECIMAL;NUMERIC;FLOAT;REAL;DOUBLE;DATE;TIME;DATETIME;TIMESTAMP;YEAR", connection);
            }

            updateDbVersion("2021-06-17", connection);
        } catch (SQLException ex) {
            System.out.println("WARNING: Release26Adapter:: unable to perform operation: " + ex.getMessage());
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("WARNING: Release26Adapter:: unable to close Statement after the operation");
                }
            }
        }

        System.out.println("SUCCESS: Release26Adapter:: has been successfully finished");

        return true;
    }

    private void insertDbParamValue(String keyName, String value, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("insert into apricot_app_parameter (app_parameter_name, app_parameter_value) values (?, ?)");
        stmt.setString(1, keyName);
        stmt.setString(2, value);
        stmt.execute();

        stmt.close();
    }

    private void updateDbVersion(String dbVersion, Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("update apricot_app_parameter set app_parameter_value=? where app_parameter_name=?");
        stmt.setString(1, dbVersion);
        stmt.setString(2, "database_version");
        stmt.execute();

        stmt.close();
    }
}
