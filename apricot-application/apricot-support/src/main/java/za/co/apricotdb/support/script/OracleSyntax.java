package za.co.apricotdb.support.script;

import za.co.apricotdb.support.util.FieldAttributeHelper;

/**
 * The Oracle specific syntax implementation.
 * 
 * @author Anton Nazarov
 * @since 24/11/2019
 */
public class OracleSyntax implements SqlSyntax {

    private SqlSyntax defaultSyntax;

    public OracleSyntax() {
        defaultSyntax = new DefaultSqlSyntax();
    }

    @Override
    public String alterColumn(String tableName, String columnName, String dataType, String valueLength,
            boolean nullable) {
        StringBuilder sb = new StringBuilder();

        // this syntax is specific for ORACLE 10G and later
        sb.append("alter table ").append(tableName).append("\n");
        sb.append(SqlScriptGenerator.INDENT).append("modify ").append(columnName).append(" ").append(dataType);
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
        return defaultSyntax.dropIndex(tableName, indexName);
    }

    @Override
    public String dropConstraint(String tableName, String constraintName) {
        return defaultSyntax.dropConstraint(tableName, constraintName);
    }
}
