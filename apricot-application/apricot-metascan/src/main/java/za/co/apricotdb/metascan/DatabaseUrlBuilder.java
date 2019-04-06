package za.co.apricotdb.metascan;

/**
 * All JDBC URL builders have to implement this interface.
 * 
 * @author Anton Nazarov
 * @since 06/04/2019
 */
public interface DatabaseUrlBuilder {
    
    String getDriverClass();
}
