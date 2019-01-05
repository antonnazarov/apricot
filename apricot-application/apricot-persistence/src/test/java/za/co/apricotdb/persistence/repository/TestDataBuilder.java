package za.co.apricotdb.persistence.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.ConstraintType;

/**
 * A creator of testing data.
 *
 * @author Anton Nazarov
 * @since 24/09/2018
 */
public class TestDataBuilder {

    @Resource
    private ApricotTableRepository tableRepository;

    @Resource
    private ApricotRelationshipRepository relationshipRepository;

    /**
     * Create the testing data.
     */
    public void createTestData() throws TestDataBuilderException {
        
        List<ApricotTable> t = tableRepository.findAll();
        if (t != null && t.size() > 0) {
            throw new TestDataBuilderException("The database is not empty!");
        }
        
        List<ApricotSnapshot> snapshots = new ArrayList<>();
        List<ApricotProjectParameter> parameters = new ArrayList<>();
        List<ApricotView> views = new ArrayList<>();
        ApricotProject project = new ApricotProject("TEST_PROJ", "The test project description", 
                "MSSQL", true, new java.util.Date(), snapshots, parameters, views);
        List<ApricotTable> tables = new ArrayList<>();
        ApricotSnapshot snapshot = new ApricotSnapshot("Test snapshot", new java.util.Date(), new java.util.Date(), 
                "Test comment", true, project, tables);
        snapshots.add(snapshot);

        ApricotTable person = createPerson(snapshot);
        ApricotTable department = createDepartment(snapshot);
        ApricotTable language = createLanguage(snapshot);
        ApricotTable languageRef = createLanguageRef(snapshot);

        tableRepository.save(person);
        tableRepository.save(department);
        tableRepository.save(language);
        tableRepository.save(languageRef);

        //  relationships
        ApricotRelationship rPerson1 = department.establishChildConstraint("department_pk", person, "department_fk");
        ApricotRelationship rPerson2 = language.establishChildConstraint("language_pk", person, "language_fk");
        ApricotRelationship rLanguage = languageRef.establishChildConstraint("language_ref_pk", language, "language_ref_fk");

        relationshipRepository.save(rPerson1);
        relationshipRepository.save(rPerson2);
        relationshipRepository.save(rLanguage);
    }

    private ApricotTable createPerson(ApricotSnapshot snapshot) {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Person", columns, constraints, snapshot);

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

    private ApricotTable createDepartment(ApricotSnapshot snapshot) {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Department", columns, constraints, snapshot);
        ApricotColumn column = new ApricotColumn("department_id", 1, false, "long", null, table);
        columns.add(column);
        column = new ApricotColumn("department_name", 2, false, "varchar", "100", table);
        columns.add(column);

        ApricotConstraint constraint = new ApricotConstraint("department_pk", ConstraintType.PRIMARY_KEY, table, "department_id");
        constraints.add(constraint);

        return table;
    }

    private ApricotTable createLanguage(ApricotSnapshot snapshot) {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Language", columns, constraints, snapshot);
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

    private ApricotTable createLanguageRef(ApricotSnapshot snapshot) {
        List<ApricotColumn> columns = new ArrayList<>();
        List<ApricotConstraint> constraints = new ArrayList<>();
        ApricotTable table = new ApricotTable("Language_Ref", columns, constraints, snapshot);
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
