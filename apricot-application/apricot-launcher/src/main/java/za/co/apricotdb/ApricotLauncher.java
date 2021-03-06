package za.co.apricotdb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This is a simple launcher of the Apricot application.
 *
 * @author Anton Nazarov
 * @since 24/02/2019
 */
public class ApricotLauncher {

    public static void main(String[] args) {
        System.out.println("Launching...");

        ApricotLauncher launcher = new ApricotLauncher();
        if (!launcher.checkIfDatabaseFileExists("./data/apricot-project.mv.db")) {
            System.out.println("The reference database file was not found. Exiting the Apricot Launcher with no " +
                    "database check");
            return;
        }

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: unable to initialize the DB driver");
            e.printStackTrace();
            System.exit(1);
        }

        String referenceVersion = null;
        try {
            referenceVersion = launcher.getDatabaseVersion("./data/apricot-project");
            System.out.println("INFO: version of the reference database=[" + referenceVersion + "]");
        } catch (Exception e) {
            System.out.println("ERROR: unable to retrieve version of the reference database");
            e.printStackTrace();
            System.exit(1);
        }

        if (!launcher.checkIfTargetDirExists()) {
            launcher.createTargetDir();
            try {
                launcher.copyReferenceToTarget();
            } catch (IOException e) {
                System.out.println("ERROR: unable to copy the project database from reference to target");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            if (!launcher.checkIfDatabaseFileExists(System.getProperty("user.home") + "/.apricotdb/apricot-project.mv.db")) {
                try {
                    launcher.copyReferenceToTarget();
                    System.out.println("INFO: new database was copied to the apricotdb catalogue");
                } catch (IOException e) {
                    System.out.println("ERROR: unable to copy the project database from reference to target");
                    e.printStackTrace();
                    System.exit(1);
                }
            } else {
                String targetVersion = null;
                try {
                    targetVersion = launcher.getDatabaseVersion(System.getProperty("user.home") + "/.apricotdb/apricot-project");
                    System.out.println("INFO: version of the target database=[" + targetVersion + "]");

                    if (!targetVersion.equals(referenceVersion)) {
                        System.out.println("INFO: the versions of the reference- and target-databases are " +
                                "different. Trying to run adapters to align the database structure");
                        launcher.createBackup();
                        AdapterRunner runner = new AdapterRunner();
                        if (!runner.runAdapters()) {
                            System.out.println("WARNING: the versions of the reference- and target-databases are " +
                                    "different. Since the Adapters failed to fix the database structure, replacing " +
                                    "the [" + targetVersion + "] with [" + referenceVersion + "]");
                            launcher.copyReferenceToTarget();
                        }
                    } else {
                        System.out.println("SUCCESS: the version of the target DB is up-to-date");
                    }
                } catch (Exception e) {
                    System.out.println("ERROR: unable to retrieve version of the target database");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

        System.out.println("SUCCESS: the Apricot Project Database has been checked and up-to-date");
    }

    private boolean checkIfDatabaseFileExists(String dbFile) {
        File f = new File(dbFile);
        if (f.exists()) {
            return true;
        }

        return false;
    }

    private String getDatabaseVersion(String dbFile) throws Exception {
        String version = null;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:" + dbFile, "", "");
            ps = conn.prepareStatement(
                    "select app_parameter_value from apricot_app_parameter where app_parameter_name=?");
            ps.setString(1, "database_version");
            rs = ps.executeQuery();
            if (rs.next()) {
                version = rs.getString("app_parameter_value");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return version;
    }

    private boolean checkIfTargetDirExists() {
        File f = new File(System.getProperty("user.home") + "/.apricotdb");
        if (f.exists()) {
            return true;
        }

        return false;
    }

    private void createTargetDir() {
        File f = new File(System.getProperty("user.home") + "/.apricotdb");
        f.mkdir();
    }

    private void copyReferenceToTarget() throws IOException {
        Files.copy(new File("./data/apricot-project.mv.db").toPath(),
                new File(System.getProperty("user.home") + "/.apricotdb/apricot-project.mv.db").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }

    private void createBackup() throws IOException {
        Files.copy(new File(System.getProperty("user.home") + "/.apricotdb/apricot-project.mv.db").toPath(),
                new File(System.getProperty("user.home") + "/.apricotdb/apricot-project.mv.db.backup").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
