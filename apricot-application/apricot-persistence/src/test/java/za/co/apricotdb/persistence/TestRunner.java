package za.co.apricotdb.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

/**
 * A standalone application to run some tests.
 * 
 * @author Anton Nazarov
 * @since 25/09/2018
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@ActiveProfiles("test")
public class TestRunner {
    public static void main(String[] args) {
        System.out.println("The standalone test was started");
        
        SpringApplication.run(TestRunner.class, args);
        
        System.out.println("The standalone test was finished");
    }
}
