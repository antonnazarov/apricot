package za.co.apricotdb.repository;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import za.co.apricotdb.entity.ApricotColumn;
import za.co.apricotdb.entity.ApricotConstraint;
import za.co.apricotdb.entity.ApricotTable;
import za.co.apricotdb.entity.ConstraintType;

/**
 * A creator of testing data.
 *
 * @author Anton Nazarov
 * @since 24/09/2018
 */
public class TestDataBuilder {

    @Resource
    private ApricotTableRepository tableRepository;

    /**
     * Create the testing data.
     */
    public void createTestData() {
        ApricotTable person = createPerson();
        ApricotTable department = createDepartment();
        ApricotTable language = createLanguage();
        ApricotTable languageRef = createLanguageRef();

        tableRepository.save(person);
        tableRepository.save(department);
        tableRepository.save(language);
        tableRepository.save(languageRef);
    }

    private ApricotTable createPerson() {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Person", columns, constraints);

        ApricotColumn column = new ApricotColumn("person_id", 1, false, "long", null, table);
        columns.add(column);
        column = new ApricotColumn("person_name", 2, false, "varchar", "45", table);
        columns.add(column);
        column = new ApricotColumn("person_surename", 3, false, "varchar", "55", table);
        columns.add(column);
        column = new ApricotColumn("person_age", 4, false, "int", null, table);
        columns.add(column);
        column = new ApricotColumn("department_id", 5, false, "long", null, table);
        columns.add(column);
        column = new ApricotColumn("language_id", 6, false, "long", null, table);
        columns.add(column);
        
        ApricotConstraint constraint = new ApricotConstraint("person_pk", ConstraintType.PRIMARY_KEY, table, "person_id");
        constraints.add(constraint);
        constraint = new ApricotConstraint("name_surname_unique_idx", ConstraintType.UNIQUE_INDEX, table, "person_name;person_surename");
        constraints.add(constraint);
        constraint = new ApricotConstraint("department_fk", ConstraintType.FOREIGN_KEY, table, "department_id");
        constraints.add(constraint);
        constraint = new ApricotConstraint("language_fk", ConstraintType.FOREIGN_KEY, table, "language_id");
        constraints.add(constraint);

        return table;
    }

    private ApricotTable createDepartment() {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Department", columns, constraints);
        ApricotColumn column = new ApricotColumn("department_id", 1, false, "long", null, table);
        columns.add(column);
        column = new ApricotColumn("department_name", 2, false, "varchar", "100", table);
        columns.add(column);

        ApricotConstraint constraint = new ApricotConstraint("department_pk", ConstraintType.PRIMARY_KEY, table, "department_id");
        constraints.add(constraint);
        
        return table;
    }

    private ApricotTable createLanguage() {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Language", columns, constraints);
        ApricotColumn column = new ApricotColumn("language_id", 1, false, "long", null, table);
        columns.add(column);
        column = new ApricotColumn("language_name", 2, false, "varchar", "30", table);
        columns.add(column);
        column = new ApricotColumn("language_prefix", 3, false, "int", null, table);
        columns.add(column);
        column = new ApricotColumn("language_suffix", 4, false, "int", null, table);
        columns.add(column);

        ApricotConstraint constraint = new ApricotConstraint("language_pk", ConstraintType.PRIMARY_KEY, table, "language_id");
        constraints.add(constraint);
        constraint = new ApricotConstraint("language_ref_fk", ConstraintType.FOREIGN_KEY, table, "language_prefix;language_suffix");
        constraints.add(constraint);
        
        return table;
    }

    private ApricotTable createLanguageRef() {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Language_Ref", columns, constraints);
        ApricotColumn column = new ApricotColumn("ref_prefix", 1, false, "int", null, table);
        columns.add(column);
        column = new ApricotColumn("ref_suffix", 2, false, "int", null, table);
        columns.add(column);
        column = new ApricotColumn("ref_definition", 3, false, "varchar", "1000", table);
        columns.add(column);
        
        ApricotConstraint constraint = new ApricotConstraint("language_ref_pk", ConstraintType.PRIMARY_KEY, table, "ref_prefix;ref_suffix");
        constraints.add(constraint);

        return table;
    }
}
