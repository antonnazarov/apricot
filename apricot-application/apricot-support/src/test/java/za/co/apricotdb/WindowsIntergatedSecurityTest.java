package za.co.apricotdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This testing stand alone class tries to access SQLServer database using the
 * native Windows authentication.
 * 
 * @author Anton Nazarov
 * @since 28/02/2020
 */
public class WindowsIntergatedSecurityTest {

    public static void main(String[] args) {
        WindowsIntergatedSecurityTest test = new WindowsIntergatedSecurityTest();
        // test.connectClassic();
        test.testConnection();
    }

    /**
     * See sqljdbc_auth.dll
     */
    public void testConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=MyDatabase;integratedSecurity=true");
            PreparedStatement ps = conn.prepareStatement("select * from audit");
            ResultSet rs = ps.executeQuery();
            System.out.println("The native access!!");
            while (rs.next()) {
                System.out.println(rs.getLong("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectClassic() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=MyDatabase",
                    "Intermediary_Compensation_Read", "$@TionRe@d01");
            PreparedStatement ps = conn.prepareStatement("select * from audit");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getLong("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
