package za.co.apricotdb.persistence;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is the test for the Service based connection to the ORACLE database. This test might fail if the test
 * database is
 * down.
 *
 * @author Anton Nazarov
 * @since 22/05/2021
 */
public class OracleConnectionServiceTest {

    static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    static final String USER = "test_user";
    static final String PASS = "giantant100";
    static final String QUERY = "select * from test_user.demo_orders";

    @Test
    public void testSidConnectionToOracle() {
        System.out.println("Testing the Service based Oracle connection...");
        // Open a connection
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);) {
            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.println("ORDER ID: " + rs.getInt("order_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
