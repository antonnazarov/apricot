package za.co.apricotdb.metascan;

/**
 * The databases, which has been supported by Apricot.
 * 
 * @author Anton Nazarov
 * @since 05/02/2019
 */
public enum ApricotTargetDatabase {
    MSSQLServer(true), Oracle(true), MySQL(true), PostrgeSQL(true), H2(true);

    private boolean supported;

    ApricotTargetDatabase(boolean supported) {
        this.supported = supported;
    }

    public boolean isSupported() {
        return supported;
    }
}
