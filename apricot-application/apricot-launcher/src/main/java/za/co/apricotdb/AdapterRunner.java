package za.co.apricotdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This is a runner for all Db adapters implemented and registered.
 *
 * @author Anton Nazarov
 * @since 10/07/2020
 */
public class AdapterRunner {
    public static final DatabaseStructureAdapter[] adapters = {new Release23Adapter()};

    public boolean runAdapters() {
        String dbFile = System.getProperty("user.home") + "/.apricotdb/apricot-project";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + dbFile, "", "");
            for (DatabaseStructureAdapter adapter : adapters) {
                if (!adapter.adapt(connection)) {
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Database Adapter:: unable to establish connection to the database [" + dbFile + "], " + ex.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println("Database Adapter:: unable to close connection: " + ex.getMessage());
                }
            }
        }

        return true;
    }
}
