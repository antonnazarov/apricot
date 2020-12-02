package za.co.apricotdb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This adapter extends the field data_type of apricot_column table to 45 symbols (the issue reported on SourceForge N 31).
 *
 * alter table apricot_column
 * alter column data_type varchar(45) not null;
 */
public class Release24MR1Adapter implements DatabaseStructureAdapter {

    @Override
    public boolean adapt(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("alter table apricot_column alter column data_type varchar(45) not null");
            statement.execute("update apricot_app_parameter set app_parameter_value='2020-12-02' where " +
                    "app_parameter_name='database_version'");
        } catch (SQLException ex) {
            System.out.println("WARNING: Release24MR1Adapter:: unable to perform operation: " + ex.getMessage());
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("WARNING: Release24MR1Adapter:: unable to close Statement after the " +
                            "operation");
                }
            }
        }

        System.out.println("SUCCESS: Release24MR1Adapter:: has been successfully finished");

        return true;
    }
}
