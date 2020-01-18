package za.co.apricotdb.support.util;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.MetaDataScannerFactory;
import za.co.apricotdb.persistence.data.DataSaver;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.repository.ApricotSnapshotRepository;

/**
 * This component calls the meta- scanner.
 *
 * @author Anton Nazarov
 * @since 01/10/2018
 */
@Component
@Transactional
public class CmdMetaScanner implements CommandLineRunner {

    @Autowired
    MetaDataScannerFactory scannerFactory;

    @Autowired
    DataSaver dataSaver;

    @Autowired
    ApricotSnapshotRepository snapshotRepository;

    @Override
    public void run(String... args) throws Exception {
        for (String c : args) {
            // the expected command line:
            // -Dexec.args="scan driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
            // url=jdbc:sqlserver://localhost:1433;databaseName=Intermediary_Account
            // user=Intermediary_Account password=password"
            if (c.equals("scan")) {
                Properties props = getConnectionParameters(args);
                if (!checkConnectionParameters(props)) {
                    System.out.println("Unable to recognize the connection parameters from the given arguments");
                    return;
                }

                String driver = props.getProperty("driver");
                String url = props.getProperty("url");
                String schema = props.getProperty("schema");
                String user = props.getProperty("user");
                String password = props.getProperty("password");
                String snapshot = props.getProperty("snapshot");

                MetaDataScanner scanner = scannerFactory.getScanner(ApricotTargetDatabase.MSSQLServer);
                if (scanner == null) {
                    System.out.println("Unable to find scanner for url=[" + url + "]");
                    return;
                }

                System.out.println("Scanning the database for the following parameters: driver=[" + driver + "], url=["
                        + url + "], user=[" + user + "], password=[" + password + "], snapshot=[" + snapshot + "]");
                MetaData result = scanner.scan(ApricotTargetDatabase.MSSQLServer, driver, url, user, schema, password, getSnapshot(Long.parseLong(snapshot)));
                System.out.println("The scanned results:");
                System.out.println(result);

                dataSaver.saveMetaData(result);
                System.out.println("Scanner was successfully called. The scanner results were serialized");

                break;
            }
        }
    }

    private ApricotSnapshot getSnapshot(long snapshotId) {
        return snapshotRepository.getOne(snapshotId);
    }

    /**
     * Read the properties of the database connection from the command line.
     */
    private Properties getConnectionParameters(String... args) {
        Properties props = new Properties();
        for (String c : args) {
            if (c.startsWith("driver=") || c.startsWith("schema=") || c.startsWith("user=") || c.startsWith("password=")
                    || c.startsWith("snapshot=")) {
                String[] r = c.split("=");
                if (r.length == 2) {
                    props.setProperty(r[0], r[1]);
                } else {
                    props.setProperty(r[0], "");
                }
            } else if (c.startsWith("url=")) {
                props.setProperty("url", c.substring(4));
            }
        }

        return props;
    }

    private boolean checkConnectionParameters(Properties props) {
        if (props.getProperty("driver") == null || props.getProperty("url") == null || props.getProperty("user") == null
                || props.getProperty("password") == null || props.getProperty("snapshot") == null) {
            return false;
        }

        return true;
    }
}
