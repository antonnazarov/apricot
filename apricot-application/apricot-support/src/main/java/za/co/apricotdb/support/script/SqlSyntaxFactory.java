package za.co.apricotdb.support.script;

import za.co.apricotdb.metascan.ApricotTargetDatabase;

/**
 * This factory produces the proper type of syntax depending on the database
 * type.
 * 
 * @author Anton Nazarov
 * @since 24/11/2019
 */
public class SqlSyntaxFactory {

    public static SqlSyntax getSqlSyntax(ApricotTargetDatabase databaseType) {

        SqlSyntax syntax = new DefaultSqlSyntax();

        switch (databaseType) {
        case Oracle:
            syntax = new OracleSyntax();
            break;
        case MSSQLServer:
            syntax = new SqlServerSyntax();
            break;
        case MySQL:
            syntax = new MySqlSyntax();
            break;
        default:
            break;
        }

        return syntax;
    }
    
    public static SqlSyntax getGefaultSyntax() {
        return new DefaultSqlSyntax();
    }
}
