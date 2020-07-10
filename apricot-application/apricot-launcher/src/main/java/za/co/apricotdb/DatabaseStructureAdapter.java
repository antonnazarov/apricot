package za.co.apricotdb;

import java.sql.Connection;

/**
 * This class performs the calls to the adapters, which alter the database structure, when it changes in
 * the release.
 *
 * @author Anton Nazarov
 * @since 10/07/2020
 */
public interface DatabaseStructureAdapter {

    /**
     * Adapt the database structure.
     *
     * @param connection has to be initialized by the caller
     * @return true if the operation was successfully performed
     */
    boolean adapt(Connection connection);
}
