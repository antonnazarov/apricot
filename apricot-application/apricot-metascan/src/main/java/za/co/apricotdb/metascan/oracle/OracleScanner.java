
package za.co.apricotdb.metascan.oracle;

import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.MetaDataScanner;
import za.co.apricotdb.persistence.data.MetaData;

/**
 * Scanner for the Oracle database.
 * 
 * @author Anton Nazarov
 * @since 04/10/2018
 */
@Component
public class OracleScanner implements MetaDataScanner {

    @Override
    public MetaData scan(String driverClassName, String url, String userName, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}