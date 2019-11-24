package za.co.apricotdb.support.script;

/**
 * The default sql syntax. Applicabe when no database specific syntax is
 * provided.
 * 
 * @author Anton Nazarov
 * @since 24/11/2019
 */
public class DefaultSqlSyntax implements SqlSyntax {
    
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
