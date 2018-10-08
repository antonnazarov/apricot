package za.co.apricotdb.support.excel;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

public class TableWrapperTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testShortTable() {
        ApricotTable shortTable = new ApricotTable();
        shortTable.setName("TARGET_TABLE");
        ApricotColumn clmn01 = new ApricotColumn("id", 1, false, "int", "0", shortTable);
        ApricotColumn clmn02 = new ApricotColumn("party_definition", 2, false, "varchar", "50", shortTable);
        ApricotColumn clmn03 = new ApricotColumn("party_organizer_code", 3, false, "varchar", "10", shortTable);
        ApricotColumn clmn04 = new ApricotColumn("party_date", 4, false, "date", "0", shortTable);
        ApricotColumn clmn05 = new ApricotColumn("party_client", 5, false, "varchar", "100", shortTable);
        ApricotColumn clmn06 = new ApricotColumn("party_price", 6, false, "float", "10.2", shortTable);
        List<ApricotColumn> columns = new ArrayList<>();
        columns.add(clmn01);
        columns.add(clmn02);
        columns.add(clmn03);
        columns.add(clmn04);
        columns.add(clmn05);
        columns.add(clmn06);
        shortTable.setColumns(columns);
        ApricotConstraint c1 = new ApricotConstraint();
        c1.setType(ConstraintType.PRIMARY_KEY);
        c1.setTable(shortTable);
        ApricotColumnConstraint acc1 = new ApricotColumnConstraint(c1, clmn01);
        ApricotConstraint c2 = new ApricotConstraint();
        c2.setType(ConstraintType.FOREIGN_KEY);
        c2.setTable(shortTable);
        ApricotColumnConstraint acc2 = new ApricotColumnConstraint(c2, clmn03);
        ApricotColumnConstraint acc3 = new ApricotColumnConstraint(c2, clmn04);
        ApricotConstraint c3 = new ApricotConstraint();
        c3.setType(ConstraintType.UNIQUE_INDEX);
        c3.setTable(shortTable);
        ApricotColumnConstraint acc4 = new ApricotColumnConstraint(c3, clmn03);
        ApricotColumnConstraint acc5 = new ApricotColumnConstraint(c3, clmn04);
        ApricotColumnConstraint acc6 = new ApricotColumnConstraint(c3, clmn05);
        ApricotColumnConstraint acc7 = new ApricotColumnConstraint(c3, clmn06);

        List<ApricotColumnConstraint> cclnms = new ArrayList<>();
        cclnms.add(acc1);
        cclnms.add(acc2);
        cclnms.add(acc3);
        cclnms.add(acc4);
        cclnms.add(acc5);
        cclnms.add(acc6);
        cclnms.add(acc7);

        List<ApricotConstraint> constraints = new ArrayList<>();
        constraints.add(c1);
        constraints.add(c2);
        constraints.add(c3);
        shortTable.setConstraints(constraints);

        ApricotTable parent1 = new ApricotTable();
        parent1.setName("PARENT_TABLE_1");
        ApricotTable parent2 = new ApricotTable();
        parent2.setName("PARENT_TABLE_2");
        ApricotTable child1 = new ApricotTable();
        child1.setName("CHILD_TABLE_1");

        ApricotConstraint cp1 = new ApricotConstraint("Parent_1", ConstraintType.PRIMARY_KEY, parent1);
        ApricotConstraint cp2 = new ApricotConstraint("Parent_2", ConstraintType.PRIMARY_KEY, parent2);
        ApricotConstraint cc1 = new ApricotConstraint("Parent_2", ConstraintType.FOREIGN_KEY, child1);
        ApricotRelationship r1 = new ApricotRelationship(c1, cp1);
        ApricotRelationship r2 = new ApricotRelationship(c1, cp2);
        ApricotRelationship r3 = new ApricotRelationship(c1, cc1);
        List<ApricotRelationship> rr = new ArrayList<>();
        rr.add(r1);
        rr.add(r2);
        rr.add(r3);

        TableWrapper w = new TableWrapper(shortTable, rr);
        System.out.println(w);
    }
}
