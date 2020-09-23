package za.co.apricotdb.metascan.sqlite;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the ParsedTable class.
 *
 * @author Anton Nazarov
 * @since 22/09/2020
 */
public class ParsedTableTest {

    @Test
    public void noPrimaryKey() throws Exception {
        String sql = "CREATE TABLE DAMAN2 (\n" +
                "    ID int NOT NULL,\n" +
                "    LastName varchar(255) NOT NULL,\n" +
                "    FirstName varchar(255),\n" +
                "    Age int,\n" +
                "    CONSTRAINT UC_Person UNIQUE (ID,LastName)\n" +
                ");";
        Statement statement = CCJSqlParserUtil.parse(sql);
        assertTrue(statement instanceof CreateTable);

        CreateTable ct = (CreateTable) statement;
        SqliteParseHelper.printCreateTable(ct);
    }

    @Test
    public void primaryKeyEmbed() throws Exception {
        String sql = "CREATE TABLE DAMAN4 (\n" +
                "    ID int,\n" +
                "    SOCIAL_NUM varchar(20),\n" +
                "    FirstName varchar(255),\n" +
                "    LastName varchar(255) NOT NULL,\n" +
                "    Hobby varchar(150),\n" +
                "    Age int,\n" +
                "    primary key(ID, SOCIAL_NUM)\n" +
                ")";
        Statement statement = CCJSqlParserUtil.parse(sql);
        assertTrue(statement instanceof CreateTable);

        CreateTable ct = (CreateTable) statement;
        SqliteParseHelper.printCreateTable(ct);
    }

    @Test
    public void foreignKey() throws Exception {
        String sql = "CREATE TABLE person_feature_history (\n" +
                "   value_id int not null,\n" +
                "   feature_value varchar(1000) not null,\n" +
                "   created_date date not null,\n" +
                "   changed_date date not null,\n" +
                "   foreign key(value_id) references person_feature_value(id)\n" +
                ")";
        Statement statement = CCJSqlParserUtil.parse(sql);
        assertTrue(statement instanceof CreateTable);

        CreateTable ct = (CreateTable) statement;
        SqliteParseHelper.printCreateTable(ct);
    }

    @Test
    public void primaryKeyField() throws Exception {
        String sql = "CREATE TABLE payment_transaction (\n" +
                "   id int primary key,\n" +
                "   transaction_period varchar(7) not null,\n" +
                "   transaction_started_date date not null,\n" +
                "   transaction_finished_date date,\n" +
                "   transaction_overall_amount float not null,\n" +
                "   transaction_approved bit(1) not null,\n" +
                "   transaction_type int not null,\n" +
                "   foreign key(transaction_type) references payment_transaction_type(id)\n" +
                ")";
        Statement statement = CCJSqlParserUtil.parse(sql);
        assertTrue(statement instanceof CreateTable);

        CreateTable ct = (CreateTable) statement;
        SqliteParseHelper.printCreateTable(ct);
    }

    @Test
    public void uniqueNoName() throws Exception {
        String sql = "CREATE TABLE DAMAN (\n" +
                "    ID int NOT NULL,\n" +
                "    LastName varchar(255) NOT NULL,\n" +
                "    FirstName varchar(255),\n" +
                "    Age int,\n" +
                "    UNIQUE (ID)\n" +
                ")";
        Statement statement = CCJSqlParserUtil.parse(sql);
        assertTrue(statement instanceof CreateTable);

        CreateTable ct = (CreateTable) statement;
        SqliteParseHelper.printCreateTable(ct);
    }
}
