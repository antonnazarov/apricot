package za.co.apricotdb.metascan;

/**
 * The main interface of Meta Scan - the scanner of the DB- structure.
 *
 * @author Anton Nazarov
 * @since 26/09/2018
 */
public interface MetaDataScanner {

    StructureScanned scan(String driverClassName, String url, String userName, String password);
}
