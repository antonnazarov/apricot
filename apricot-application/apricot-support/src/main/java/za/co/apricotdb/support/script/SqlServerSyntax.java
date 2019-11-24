package za.co.apricotdb.support.script;

public class SqlServerSyntax implements SqlSyntax {

    private SqlSyntax defaultSyntax;

    public SqlServerSyntax() {
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
