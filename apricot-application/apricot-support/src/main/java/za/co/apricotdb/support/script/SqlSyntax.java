package za.co.apricotdb.support.script;

/**
 * The database specific syntax for database specific commands.
 * 
 * @author Anton Nazarov
 * @since 24/11/2019
 */
public interface SqlSyntax {

    String alterColumn(String tableName, String columnName, String dataType, String valueLength, boolean nullable);

    String dropUniqueConstraint(String tableName, String constraintName);

    String dropForeignKeyConstraint(String tableName, String constraintName);

    String dropPrimaryKeyConstraint(String tableName, String constraintName);

    String dropIndex(String tableName, String indexName);
    
    String dropConstraint(String tableName, String constraintName);
}
