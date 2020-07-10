package za.co.apricotdb;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The db structure Adapter for Release 2.3.
 *
 * @author Anton Nazarov
 * @since 10/07/2020
 */
public class Release23Adapter implements DatabaseStructureAdapter {

    @Override
    public boolean adapt(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("alter table apricot_app_parameter alter column app_parameter_value varchar");
            statement.execute("update apricot_app_parameter set app_parameter_value='2020-07-09' where " +
                    "app_parameter_name='database_version'");
        } catch (SQLException ex) {
            System.out.println("WARNING: Release23Adapter:: unable to perform operation: " + ex.getMessage());
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("WARNING: Release23Adapter:: unable to close Statement after the " +
                            "operation");
                }
            }
        }

        System.out.println("SUCCESS: Release23Adapter:: has been successfully finished");

        return true;
    }
}
