package za.co.apricotdb.metascan.sqlite;

import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import org.junit.Before;
import org.junit.Test;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SqliteParserTest {

    private SqliteParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new SqliteParser();
    }

    @Test
    public void testParse() {
        ParsedBundle parsedBundle = new ParsedBundle();
        ApricotSnapshot snapshot = new ApricotSnapshot();
        parser.parseSql("CREATE TABLE payroll (\n" +
                "   payroll_id int primary key,\n" +
                "   person_id int not null, -- FK from person\n" +
                "   payment_transaction_id int not null, -- FK from payment_transaction\n" +
                "   payment_code varchar(10) not null, -- FK from payment_code\n" +
                "   payment_period varchar(7) not null, -- formatted as MM/YYYY\n" +
                "   payment_amount float not null,\n" +
                "   constraint payroll_code_unique unique (person_id, payment_transaction_id, payment_code, payment_period),\n" +
                "   foreign key(person_id) references person(person_id),\n" +
                "   foreign key(payment_transaction_id) references payment_transaction(id),\n" +
                "   foreign key(payment_code) references payment_code(pcode)\n" +
                ")", parsedBundle, snapshot);
        ParsedTable parsedTable = parsedBundle.getParsedTable("payroll");
        assertNotNull(parsedTable);
        ApricotTable table = parsedTable.getTable();
        for (ApricotColumn c : table.getColumns()) {
            System.out.println(c.toString());
        }
        for (ApricotConstraint c : table.getConstraints()) {
            System.out.println(c.toString());
        }

        assertEquals(3, parsedTable.getForeignKeys().size());
    }

    @Test
    public void testCreateIndex() {
        ParsedBundle parsedBundle = new ParsedBundle();
        ApricotSnapshot snapshot = new ApricotSnapshot();
        String person = "CREATE TABLE person (\n" +
                "   person_id int primary key,\n" +
                "   tabel_nummer varchar(10) not null,\n" +
                "   first_name varchar(50) not null,\n" +
                "   second_name varchar(50) not null,\n" +
                "   religion varchar(50),  -- FK from religion\n" +
                "   department varchar(20) not null, -- FK from department\n" +
                "   manager int, -- auto FK from person\n" +
                "   position_code varchar(10) not null, -- FK from position\n" +
                "   position_suffix varchar(5) not null, -- FK from position\n" +
                "   foreign key (religion) references religion(religion_name),\n" +
                "   foreign key(department) references department(department_code),\n" +
                "   foreign key(manager) references person(person_id)\n" +
                ")";
        String pcode = "CREATE TABLE pcode_in_payment_group (\n" +
                "   pcode varchar(10) not null,\n" +
                "   payment_group_id int not null,\n" +
                "   foreign key(payment_group_id) references payment_group(group_id),\n" +
                "   foreign key(pcode) references payment_code(pcode)\n" +
                ")";
        parser.parseSql(person, parsedBundle, snapshot);
        parser.parseSql(pcode, parsedBundle, snapshot);
        parser.parseSql("CREATE INDEX person_name_idx on person(first_name, second_name)", parsedBundle, snapshot);
        parser.parseSql("CREATE UNIQUE INDEX pcode_in_payment_group_idx on pcode_in_payment_group (pcode, payment_group_id)", parsedBundle, snapshot);
        List<ParsedTable> parsedTables = parsedBundle.getParsedTables();
        assertEquals(2, parsedTables.size());

        ParsedTable pTable = parsedBundle.getParsedTable("person");
        assertNotNull(pTable);
        System.out.println(pTable.getTable().getConstraints());

        for (ForeignKeyIndex fk :pTable.getForeignKeys()) {
            System.out.println("FK: table: " + fk.getTable() + " columns: " + fk.getColumnsNames() +
                    " ref columns: " + fk.getReferencedColumnNames());
        }

        pTable = parsedBundle.getParsedTable("pcode_in_payment_group");
        assertNotNull(pTable);
        System.out.println(pTable.getTable().getConstraints());

        for (ForeignKeyIndex fk :pTable.getForeignKeys()) {
            System.out.println("FK: table: " + fk.getTable() + " columns: " + fk.getColumnsNames() +
                    " ref columns: " + fk.getReferencedColumnNames());
        }
    }
}