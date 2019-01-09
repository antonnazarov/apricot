package za.co.apricotdb.metascan;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The main interface of Meta Scan - the scanner of the DB- structure.
 *
 * @author Anton Nazarov
 * @since 26/09/2018
 */
public interface MetaDataScanner {

    MetaData scan(String driverClassName, String url, String userName, String password, ApricotSnapshot snapshot);
    
    static JdbcOperations getTargetJdbcOperations(String driverClassName, String url, String userName, String password) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);

        return new JdbcTemplate(ds);
    }
}
