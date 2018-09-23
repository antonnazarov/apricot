package za.co.apricotdb.repository;

import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import za.co.apricotdb.config.JpaDefaultConfig;
import za.co.apricotdb.entity.ApricotColumn;
import za.co.apricotdb.entity.ApricotTable;

/**
 * All sorts of repository tests.
 *
 * @author Anton Nazarov
 * @since 23/09/2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaDefaultConfig.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class RepositoryTest {

    @Resource
    private ApricotTableRepository tableRepository;
    @Resource
    private ApricotColumnRepository columnRepository;

    @Before
    public void setUp() {
        ApricotTable table = new ApricotTable();
        table.setName("Person");
        tableRepository.save(table);
        ApricotColumn column = new ApricotColumn();
        column.setName("person_id");
        column.setDataType("long");
        column.setNullable(false);
        column.setOrdinalPosition(1);
        column.setTable(table);
        columnRepository.save(column);

        table = new ApricotTable();
        table.setName("Department");
        tableRepository.save(table);

        table = new ApricotTable();
        table.setName("Language");
        tableRepository.save(table);

    }

    @Test
    public void testFindTable() {
        List<ApricotTable> tables = tableRepository.findAll();

        System.out.println(tables);

        assertNotNull(tables);
        assertEquals(3, tables.size());
    }

    @Test
    public void testFindByName() {
        ApricotTable table = tableRepository.findByName("Person");
        System.out.println(table);
        assertNotNull(table);
    }
}
