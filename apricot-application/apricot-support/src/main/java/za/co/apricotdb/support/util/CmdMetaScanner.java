package za.co.apricotdb.support.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.StructureScanned;
import za.co.apricotdb.metascan.sqlserver.SqlServerScanner;

/**
 * This component calls the metascanner.
 * 
 * @author Anton Nazarov
 * @since 01/10/2018
 */
@Component
public class CmdMetaScanner implements CommandLineRunner {

    @Autowired
    SqlServerScanner scanner;
    
    @Override
    public void run(String... args) throws Exception {
        for (String c : args) {
            if (c.equals("scan")) {
                StructureScanned result = scanner.scan("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://localhost:1433;databaseName=Intermediary_Account", "Intermediary_Account", "password");
                System.out.println(result);
                System.out.println("Scanner was successfully called");
            }
        }
    }

}
