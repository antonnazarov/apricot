package za.co.apricotdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.apricotdb.repository.TestDataBuilder;
import za.co.apricotdb.util.DataCleaner;

/**
 * Spring based runner.
 *
 * @author Anton Nazarov
 * @since 25/09/2018
 */
@Component
public class RunnerBean implements CommandLineRunner {
    
    @Autowired
    TestDataBuilder testDataBuilder;
    
    @Autowired
    DataCleaner dataCleaner;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("... inside the runner ...");
        
        dataCleaner.cleanAll();
        testDataBuilder.createTestData();
    }
}
