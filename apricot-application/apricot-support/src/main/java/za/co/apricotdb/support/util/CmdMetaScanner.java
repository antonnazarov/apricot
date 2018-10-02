package za.co.apricotdb.support.util;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.StructureScanned;
import za.co.apricotdb.metascan.sqlserver.SqlServerScanner;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.repository.ApricotRelationshipRepository;
import za.co.apricotdb.persistence.repository.ApricotTableRepository;

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

    @Resource
    ApricotTableRepository tableRepository;
    
    @Resource
    ApricotRelationshipRepository relationshipRepository;

    @Override
    public void run(String... args) throws Exception {
        for (String c : args) {
            if (c.equals("scan")) {
                StructureScanned result = scanner.scan("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://localhost:1433;databaseName=Intermediary_Account", "Intermediary_Account", "password");
                
                System.out.println(result);
                
                serializeScanResults(result);
                
                System.out.println("Scanner was successfully called");
            }
        }
    }

    private void serializeScanResults(StructureScanned result) {
        for (ApricotTable t : result.getTables()) {
            tableRepository.save(t);
        }
        
        for (ApricotRelationship r : result.getRelationships()) {
            relationshipRepository.save(r);
        }
    }
}
