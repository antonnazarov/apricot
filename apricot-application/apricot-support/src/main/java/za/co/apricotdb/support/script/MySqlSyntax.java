package za.co.apricotdb.support.script;

/**
 * The MySQL specific syntax.
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
        return defaultSyntax.alterColumn(tableName, columnName, dataType, valueLength, nullable);
    }

    @Override
    public String dropUniqueConstraint(String tableName, String constraintName) {
        return dropIndex(tableName, constraintName);
    }

    @Override
    public String dropForeignKeyConstraint(String tableName, String constraintName) {
        StringBuilder sb = new StringBuilder();

        sb.append("alter table ").append(tableName).append("\n");
        sb.append(SqlScriptGenerator.INDENT).append("drop foreign key ").append(constraintName).append(";");

        return sb.toString();
    }

    @Override
    public String dropPrimaryKeyConstraint(String tableName, String constraintName) {
        StringBuilder sb = new StringBuilder();

        sb.append("alter table ").append(tableName).append("\n");
        sb.append(SqlScriptGenerator.INDENT).append("drop primary key ").append(constraintName).append(";");

        return sb.toString();
    }

    @Override
    public String dropIndex(String tableName, String indexName) {
        StringBuilder sb = new StringBuilder();

        sb.append("alter table ").append(tableName).append("\n");
        sb.append(SqlScriptGenerator.INDENT).append("drop index ").append(indexName).append(";");

        return sb.toString();
    }

    @Override
    public String dropConstraint(String tableName, String constraintName) {
        return defaultSyntax.dropConstraint(tableName, constraintName);
    }
}
