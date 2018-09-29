package za.co.apricotdb.persistence.repository;

import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import za.co.apricotdb.persistence.config.JpaDefaultConfig;
import za.co.apricotdb.persistence.config.TestingConfiguration;
import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotTable;

/**
 * All sorts of repository tests.
 *
 * @author Anton Nazarov
 * @since 23/09/2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaDefaultConfig.class, TestingConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
@ActiveProfiles("test")
public class RepositoryTest {

    @Resource
    private ApricotTableRepository tableRepository;

    @Autowired
    TestDataBuilder testDataBuilder;

    @Before
    public void setUp() throws Exception {
        testDataBuilder.createTestData();
    }

    @Test
    public void testFindTable() {
        List<ApricotTable> tables = tableRepository.findAll();

        System.out.println(tables);

        assertNotNull(tables);
        assertEquals(4, tables.size());
    }

    @Test
    public void testFindByName() {
        ApricotTable table = tableRepository.findByName("Person");
        System.out.println(table);
        assertNotNull(table);

        List<ApricotColumn> columns = table.getColumns();
        System.out.println("Columns: " + columns);
    }
}
