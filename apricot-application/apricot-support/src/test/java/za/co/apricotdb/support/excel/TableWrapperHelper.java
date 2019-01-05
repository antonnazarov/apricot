package za.co.apricotdb.support.excel;

import java.util.ArrayList;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ConstraintType;

public class TableWrapperHelper {

    public static TableWrapper getTableWrapper() {
        ApricotTable shortTable = new ApricotTable();
        shortTable.setName("TARGET_TABLE");
        ApricotColumn clmn01 = new ApricotColumn("id", 1, false, "int", "0", shortTable);
        ApricotColumn clmn02 = new ApricotColumn("party_definition", 2, false, "varchar", "50", shortTable);
        ApricotColumn clmn03 = new ApricotColumn("party_organizer_code", 3, false, "varchar", "10", shortTable);
        ApricotColumn clmn04 = new ApricotColumn("party_date", 4, false, "date", "0", shortTable);
        ApricotColumn clmn05 = new ApricotColumn("party_client", 5, false, "varchar", "100", shortTable);
        ApricotColumn clmn06 = new ApricotColumn("party_price", 6, false, "float", "10.2", shortTable);
        ApricotColumn clmn07 = new ApricotColumn("optional_definition", 7, true, "varchar", "500", shortTable);
        ApricotColumn clmn08 = new ApricotColumn("department_column_with_very_long_name_additional_hundred_symbols", 8, true, "varchar", "500", shortTable);
        List<ApricotColumn> columns = new ArrayList<>();
        columns.add(clmn01);
        columns.add(clmn02);
        columns.add(clmn03);
        columns.add(clmn04);
        columns.add(clmn05);
        columns.add(clmn06);
        columns.add(clmn07);
        columns.add(clmn08);
        shortTable.setColumns(columns);

        // c1
        ApricotConstraint c1 = new ApricotConstraint();
        c1.setType(ConstraintType.PRIMARY_KEY);
        c1.setTable(shortTable);
        ApricotColumnConstraint acc1 = new ApricotColumnConstraint(c1, clmn01);
        List<ApricotColumnConstraint> cclnms1 = new ArrayList<>();
        cclnms1.add(acc1);
        c1.setColumns(cclnms1);

        // c2
        ApricotConstraint c2 = new ApricotConstraint();
        c2.setType(ConstraintType.FOREIGN_KEY);
        c2.setTable(shortTable);
        ApricotColumnConstraint acc2 = new ApricotColumnConstraint(c2, clmn03);
        ApricotColumnConstraint acc3 = new ApricotColumnConstraint(c2, clmn04);
        List<ApricotColumnConstraint> cclnms2 = new ArrayList<>();
        cclnms2.add(acc2);
        cclnms2.add(acc3);
        c2.setColumns(cclnms2);

        // c3
        ApricotConstraint c3 = new ApricotConstraint();
        c3.setType(ConstraintType.UNIQUE_INDEX);
        c3.setTable(shortTable);
        ApricotColumnConstraint acc4 = new ApricotColumnConstraint(c3, clmn03);
        ApricotColumnConstraint acc5 = new ApricotColumnConstraint(c3, clmn04);
        ApricotColumnConstraint acc6 = new ApricotColumnConstraint(c3, clmn05);
        ApricotColumnConstraint acc7 = new ApricotColumnConstraint(c3, clmn06);
        List<ApricotColumnConstraint> cclnms3 = new ArrayList<>();
        cclnms3.add(acc4);
        cclnms3.add(acc5);
        cclnms3.add(acc6);
        cclnms3.add(acc7);
        c3.setColumns(cclnms3);

        // c4
        ApricotConstraint c4 = new ApricotConstraint();
        c4.setType(ConstraintType.NON_UNIQUE_INDEX);
        c4.setTable(shortTable);
        ApricotColumnConstraint acc8 = new ApricotColumnConstraint(c4, clmn02);
        ApricotColumnConstraint acc9 = new ApricotColumnConstraint(c4, clmn03);
        ApricotColumnConstraint acc10 = new ApricotColumnConstraint(c4, clmn04);
        List<ApricotColumnConstraint> cclnms4 = new ArrayList<>();
        cclnms4.add(acc8);
        cclnms4.add(acc9);
        cclnms4.add(acc10);
        c4.setColumns(cclnms4);

        // c5
        ApricotConstraint c5 = new ApricotConstraint();
        c5.setType(ConstraintType.NON_UNIQUE_INDEX);
        c5.setTable(shortTable);
        ApricotColumnConstraint acc11 = new ApricotColumnConstraint(c5, clmn04);
        ApricotColumnConstraint acc12 = new ApricotColumnConstraint(c5, clmn05);
        List<ApricotColumnConstraint> cclnms5 = new ArrayList<>();
        cclnms5.add(acc11);
        cclnms5.add(acc12);
        c5.setColumns(cclnms5);

        // c6
        ApricotConstraint c6 = new ApricotConstraint();
        c6.setType(ConstraintType.UNIQUE);
        c6.setTable(shortTable);
        ApricotColumnConstraint acc13 = new ApricotColumnConstraint(c6, clmn06);
        List<ApricotColumnConstraint> cclnms6 = new ArrayList<>();
        cclnms6.add(acc13);
        c6.setColumns(cclnms6);

        List<ApricotConstraint> constraints = new ArrayList<>();
        constraints.add(c1);
        constraints.add(c2);
        constraints.add(c3);
        constraints.add(c4);
        constraints.add(c5);
        constraints.add(c6);
        shortTable.setConstraints(constraints);

        ApricotTable parent1 = new ApricotTable();
        parent1.setName("PARENT_TABLE_1");
        ApricotTable parent2 = new ApricotTable();
        parent2.setName("PARENT_TABLE_2");
        ApricotTable child1 = new ApricotTable();
        child1.setName("CHILD_TABLE_1");

        ApricotConstraint cp1 = new ApricotConstraint("Parent_1", ConstraintType.PRIMARY_KEY, parent1);
        ApricotConstraint cp2 = new ApricotConstraint("Parent_2", ConstraintType.PRIMARY_KEY, parent2);
        ApricotConstraint cc1 = new ApricotConstraint("Child_1", ConstraintType.FOREIGN_KEY, child1);
        ApricotRelationship r1 = new ApricotRelationship(cp1, c1);
        ApricotRelationship r2 = new ApricotRelationship(cp2, c1);
        ApricotRelationship r3 = new ApricotRelationship(c1, cc1);
        List<ApricotRelationship> rr = new ArrayList<>();
        rr.add(r1);
        rr.add(r2);
        rr.add(r3);

        return new TableWrapper(shortTable, rr);
    }
}
