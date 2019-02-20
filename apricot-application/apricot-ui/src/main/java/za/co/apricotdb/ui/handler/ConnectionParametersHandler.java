package za.co.apricotdb.ui.handler;

import java.util.Properties;

/**
 * This interface describes operations common for connection handlers, implemented for different databases.
 * 
 * @author Anton Nazarov
 * @since 20/02/2019
 */
public interface ConnectionParametersHandler {
    
    void saveConnectionParameters(Properties params);
}
