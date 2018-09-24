package za.co.apricotdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import za.co.apricotdb.repository.TestDataBuilder;

/**
 * This configuration class acts only on the resting phase.
 * 
 * @author Anton Nazarov
 * @since 24/09/2018
 */
@Configuration
public class TestingConfiguration {
    
    @Bean
    TestDataBuilder getTestDataBuilder() {
        return new TestDataBuilder();
    }
}
