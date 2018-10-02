package za.co.apricotdb.support.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.util.DataCleaner;

/**
 * Clean the Apricot- database.
 * 
 * @author Anton Nazarov
 * @since 29/09/2018
 */
@Component
public class CmdDatabaseCleaner implements CommandLineRunner {

    @Autowired
    DataCleaner dataCleaner;
    
    @Override
    public void run(String... args) throws Exception {
        for (String c : args) {
            if (c.equals("clean")) {
                dataCleaner.cleanAll();
                System.out.println("The DB was cleaned successfully");
            }
        }
    }
}
