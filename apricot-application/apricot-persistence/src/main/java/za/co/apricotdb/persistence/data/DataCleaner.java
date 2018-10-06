package za.co.apricotdb.persistence.data;

import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

/**
 * Cleans all Apricot- project data.
 *
 * @author Anton Nazarov
 * @since 26/09/2018
 */
@Component
public class DataCleaner {

    @Resource
    JdbcOperations jdbcOperations;

    public void cleanAll() {
        jdbcOperations.batchUpdate(
                "delete from apricot_column_in_constraint;",
                "delete from apricot_relationship;",
                "delete from apricot_constraint;",
                "delete from apricot_column;",
                "delete from apricot_table;"
        );
    }
}
