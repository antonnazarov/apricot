package za.co.apricotdb.metascan.sqlite;

import org.junit.Before;
import org.junit.Test;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;

import static org.junit.Assert.*;

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
        parser.parseSql("CREATE INDEX person_name_idx on person(first_name, second_name)", parsedBundle, snapshot);
        parser.parseSql("CREATE UNIQUE INDEX pcode_in_payment_group_idx on pcode_in_payment_group (pcode, payment_group_id)", parsedBundle, snapshot);
    }
}