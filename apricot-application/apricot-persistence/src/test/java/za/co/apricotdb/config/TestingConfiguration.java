package za.co.apricotdb.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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
    TestDataBuilder testDataBuilder() {
        return new TestDataBuilder();
    }
    
    @Bean
    @Profile("test")
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:apricot-project-structure.sql")
                .build();
    }
}
