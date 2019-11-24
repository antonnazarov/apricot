package za.co.apricotdb.support.script;

/**
 * The NySQL specific syntax.
 * 
 * @author Anton Nazarov
 * @since 24/11/2019
 */
public class MySqlSyntax implements SqlSyntax {

    private SqlSyntax defaultSyntax;

    public MySqlSyntax() {
        defaultSyntax = new DefaultSqlSyntax();
    }

    @Override
    public String alterColumn(String tableName, String columnName, String dataType, String valueLength,
            boolean nullable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String dropUniqueConstraint(String tableName, String constraintName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String dropForeignKeyConstraint(String tableName, String constraintName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String dropPrimaryKeyConstraint(String tableName, String constraintName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String dropIndex(String tableName, String indexName) {
        // TODO Auto-generated method stub
        return null;
    }
}
