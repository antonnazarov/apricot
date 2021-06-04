package za.co.apricotdb.metascan;

/**
 * The databases, which has been supported by Apricot.
 * 
 * @author Anton Nazarov
 * @since 05/02/2019
 */
public enum ApricotTargetDatabase {
    MSSQLServer("MSSQLServer", true), Oracle("Oracle", true), MySQL("MySQL", true), PostrgeSQL("PostgreSQL", true),
    DB2("DB2", true), DB2_LUW("DB2 (LUW)", true), H2("H2", true), SQLite("SQLite", true),
    MariaDB("MariaDB", true);

    private boolean supported;
    private String databaseName;

    ApricotTargetDatabase(String databaseName, boolean supported) {
        this.databaseName = databaseName;
        this.supported = supported;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public boolean isSupported() {
        return supported;
    }

    public static ApricotTargetDatabase parse(String name) {
        ApricotTargetDatabase ret = null;
        for (ApricotTargetDatabase tb : ApricotTargetDatabase.values()) {
            if (name.equals(tb.getDatabaseName())) {
                ret = tb;
                break;
            }
        }

        // if can't parse the database name, use the default one - SQLServer
        if (ret == null) {
            ret = ApricotTargetDatabase.MSSQLServer;
        }

        return ret;
    }
}
