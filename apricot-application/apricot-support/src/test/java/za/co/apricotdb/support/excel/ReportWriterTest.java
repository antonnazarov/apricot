package za.co.apricotdb.support.excel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests for the ReportWriter class.
 *
 * @author Anton Nazarov
 * @since 21/10/2018
 */
public class ReportWriterTest {

    @Test
    public void testCreateReport() throws Exception {
        TableWrapper wrapper = TableWrapperHelper.getTableWrapper();
        List<TableWrapper> wrappers = new ArrayList<>();
        wrappers.add(wrapper);
        wrappers.add(wrapper);
        wrappers.add(wrapper);
        ReportWriter rw = new ReportWriter();
        rw.createReport(wrappers, "..\\..\\apricot-reports\\ApricotReport.xlsx");
    }
}
