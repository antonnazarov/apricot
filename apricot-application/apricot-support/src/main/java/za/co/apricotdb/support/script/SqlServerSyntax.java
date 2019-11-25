package za.co.apricotdb.support.script;

import za.co.apricotdb.support.util.FieldAttributeHelper;

public class SqlServerSyntax implements SqlSyntax {

    private SqlSyntax defaultSyntax;

    public SqlServerSyntax() {
        defaultSyntax = new DefaultSqlSyntax();
    }

    @Override
    public String alterColumn(String tableName, String columnName, String dataType, String valueLength,
            boolean nullable) {
        StringBuilder sb = new StringBuilder();

        sb.append("alter table ").append(tableName).append("\n");
        sb.append(SqlScriptGenerator.INDENT).append("alter column ").append(columnName).append(" ").append(dataType);
        sb.append(FieldAttributeHelper.formFieldLength(valueLength));
        if (!nullable) {
            sb.append(" not null");
        }
        sb.append(";");

        return sb.toString();
    }

    @Override
    public String dropUniqueConstraint(String tableName, String constraintName) {
        return defaultSyntax.dropConstraint(tableName, constraintName);
    }

    @Override
    public String dropForeignKeyConstraint(String tableName, String constraintName) {
        return defaultSyntax.dropConstraint(tableName, constraintName);
    }

    @Override
    public String dropPrimaryKeyConstraint(String tableName, String constraintName) {
        return defaultSyntax.dropConstraint(tableName, constraintName);
    }

    @Override
    public String dropIndex(String tableName, String indexName) {
        StringBuilder sb = new StringBuilder();

        sb.append("drop index ").append(tableName).append(".").append(indexName).append(";");

        return sb.toString();
    }

    @Override
    public String dropConstraint(String tableName, String constraintName) {
        return defaultSyntax.dropConstraint(tableName, constraintName);
    }
}
