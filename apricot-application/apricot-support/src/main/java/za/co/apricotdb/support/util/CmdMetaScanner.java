package za.co.apricotdb.support.util;

import java.util.Properties;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.metascan.ScannerRecognizer;
import za.co.apricotdb.persistence.data.DataSaver;
import za.co.apricotdb.persistence.data.MetaData;

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
    ScannerRecognizer scannerRecognizer;
    
    @Autowired
    DataSaver dataSaver;

    @Override
    public void run(String... args) throws Exception {
        for (String c : args) {
            //  the expected command line:
            // -Dexec.args="scan driver=com.microsoft.sqlserver.jdbc.SQLServerDriver url=jdbc:sqlserver://localhost:1433;databaseName=Intermediary_Account user=Intermediary_Account password=password"
            if (c.equals("scan")) {
                Properties props = getConnectionParameters(args);
                if (!checkConnectionParameters(props)) {
                    System.out.println("Unable to recognize the connection parameters from the given arguments");
                    return;
                }
                
                String driver = props.getProperty("driver");
                String url = props.getProperty("url");
                String user = props.getProperty("user");
                String password = props.getProperty("password");
                
                MetaDataScanner scanner = scannerRecognizer.getScanner(url);
                if (scanner == null) {
                    System.out.println("Unable to find scanner for url=[" + url + "]");
                    return;
                }
                
                System.out.println("Scanning the database for the following parameters: driver=[" + driver + "], url=[" + url + "], user=[" + user + "], password=[" + password + "]");                
                MetaData result = scanner.scan(driver, url, user, password);
                System.out.println("The scanned results:");                
                System.out.println(result);
                
                dataSaver.saveMetaData(result);
                System.out.println("Scanner was successfully called. The scanner results were serialized");
                
                break;
            }
        }
    }
    
    /**
     * Read the properties of the database connection from the command line.
     */
    private Properties getConnectionParameters(String... args) {
        Properties props = new Properties();
        for (String c : args) {
            if (c.startsWith("driver=") || c.startsWith("user=") || c.startsWith("password=")) {
                String[] r = c.split("=");
                props.setProperty(r[0], r[1]);
            } else if (c.startsWith("url=")) {
                props.setProperty("url", c.substring(4));
            }
        }
        
        return props;
    }
    
    private boolean checkConnectionParameters(Properties props) {
        if (props.getProperty("driver") == null || props.getProperty("url") == null || props.getProperty("user") == null || props.getProperty("password") == null) {
            return false;
        }
        
        return true;
    }
}
