package za.co.apricotdb.support.script;

import za.co.apricotdb.support.util.FieldAttributeHelper;

/**
 * The default sql syntax. Applicable when no database specific syntax is
 * provided.
 * 
 * @author Anton Nazarov
 * @since 24/11/2019
 */
public class DefaultSqlSyntax implements SqlSyntax {

    @Override
    public String alterColumn(String tableName, String columnName, String dataType, String valueLength,
            boolean nullable) {
        StringBuilder sb = new StringBuilder();

        sb.append("alter table ").append(tableName).append("\n");
        sb.append(SqlScriptGenerator.INDENT).append("modify column ").append(columnName).append(" ").append(dataType);
        sb.append(FieldAttributeHelper.formFieldLength(valueLength));
        if (!nullable) {
            sb.append(" not null");
        }
        sb.append(";");

        return sb.toString();
    }

    @Override
    public String dropUniqueConstraint(String tableName, String constraintName) {
        return dropConstraint(tableName, constraintName);
    }

    @Override
    public String dropForeignKeyConstraint(String tableName, String constraintName) {
        return dropConstraint(tableName, constraintName);
    }

    @Override
    public String dropPrimaryKeyConstraint(String tableName, String constraintName) {
        return dropConstraint(tableName, constraintName);
    }

    @Override
    public String dropIndex(String tableName, String indexName) {
        StringBuilder sb = new StringBuilder();

        sb.append("drop index ").append(indexName).append(";");

        return sb.toString();
    }

    @Override
    public String dropConstraint(String tableName, String constraintName) {
        StringBuilder sb = new StringBuilder();

        sb.append("alter table ").append(tableName).append("\n");
        sb.append(SqlScriptGenerator.INDENT).append("drop constraint ").append(constraintName).append(";");

        return sb.toString();
    }
}
