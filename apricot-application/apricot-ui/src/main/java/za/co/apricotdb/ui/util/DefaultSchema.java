package za.co.apricotdb.ui.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.h2.H2UrlBuilder;
import za.co.apricotdb.metascan.oracle.OracleUrlBuilder;
import za.co.apricotdb.metascan.postgresql.PostgreSqlUrlBuilder;
import za.co.apricotdb.metascan.sqlserver.SqlServerUrlBuilder;

/**
 * Finds the default schema for the database type.
 * 
 * @author Anton Nazarov
 * @since 09/04/2019
 */
@Component
public class DefaultSchema {

    @Autowired
    SqlServerUrlBuilder sqlServerUrlBuilder;

    @Autowired
    H2UrlBuilder h2UrlBuilder;

    @Autowired
    OracleUrlBuilder oracleUrlBuilder;

    @Autowired
    PostgreSqlUrlBuilder postgreSqlUrlBuilder;

    public String getDefaultSchema(ApricotTargetDatabase targetDb) {
        switch (targetDb) {
        case MSSQLServer:
            return sqlServerUrlBuilder.getDefaultSchemaName();
        case H2:
            return h2UrlBuilder.getDefaultSchemaName();
        case Oracle:
            return oracleUrlBuilder.getDefaultSchemaName();
        case PostrgeSQL:
            return postgreSqlUrlBuilder.getDefaultSchemaName();
        case MySQL:
            break;
        }

        return null;
    }

}
