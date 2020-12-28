package za.co.apricotdb.metascan.sqlite;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import net.sf.jsqlparser.statement.create.table.Index;

/**
 * The helper methods for the SQLite parsing tests.
 */
public class SqliteParseHelper {

    public static void printCreateTable(CreateTable createTable) {
        System.out.println("The table object: " + createTable.getTable());
        System.out.println("Create Options: " + createTable.getCreateOptionsStrings());

        System.out.println(" ------------------- Column Definitions -------------------");
        for (ColumnDefinition cd : createTable.getColumnDefinitions()) {
            System.out.println(cd.getColumnName() + "-->" + cd.getColumnSpecs());
        }

        System.out.println(" ------------------- Indexes -------------------");
        for (Index idx : createTable.getIndexes()) {
            System.out.print(idx.getName() + "-->" + idx.getType() + ", spec:" + idx.getIndexSpec() + ", columns" +
                    ": " + idx.getColumnsNames() + ", class=" + idx.getClass().getName());
            if (idx instanceof ForeignKeyIndex) {
                ForeignKeyIndex fidx = (ForeignKeyIndex) idx;
                System.out.println(", FK: " + fidx.getTable() + "-->" + fidx.getReferencedColumnNames());
            } else {
                System.out.println();
            }
        }

        System.out.println("\n");
    }
}
