package za.co.apricotdb.support.util;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.DataReader;
import za.co.apricotdb.persistence.data.MetaData;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.support.excel.ReportWriter;
import za.co.apricotdb.support.excel.TableWrapper;

/**
 * Command line utility to generate Excel- report.
 *
 * @author Anton Nazarov
 * @since 06/10/2018
 */
@Component
public class CmdExcelReport implements CommandLineRunner {

    @Autowired
    DataReader dataReader;
    
    @Autowired
    ReportWriter reportWriter;

    /**
     * Run the Excel- report with the following parameters:
     * report tables=Table1, Table2, Table3 sortby=alphabetically
     * report tables=* sortby=weight
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (String c : args) {
            if (c.equals("report")) {
                ReportParameters params = getReportParameters(args);
                System.out.println("Generating report for the following parameters: " + params);
                MetaData result = dataReader.readTablesByList(params.tables);
                List<ApricotTable> tables = result.getTables();
                if (params.sortBy.equals("alphabetically")) {
                    sortAlphabetically(tables);
                }
                List<TableWrapper> wrappers = new ArrayList<>();
                for (ApricotTable t : tables) {
                    wrappers.add(new TableWrapper(t, result.getRelationships()));
                }
                
                reportWriter.createReport(wrappers, params.file);

                System.out.println(result);
            }
        }
    }
    
    private void sortAlphabetically(List<ApricotTable> tables) {
        tables.sort((ApricotTable t1, ApricotTable t2) -> t1.getName().compareTo(t2.getName()));
    }

    /**
     * Read detailed parameters of the report.
     */
    private ReportParameters getReportParameters(String... args) {
        ReportParameters ret = new ReportParameters();
        List<String> tables = new ArrayList();
        for (String c : args) {
            if (c.equals("tables=*")) {
                tables = new ArrayList();
                tables.add("*");
            } else if (c.contains("tables=")) {
                String sTables = c.substring(7);
                String[] aTables = sTables.split(",");
                for (String t : aTables) {
                    tables.add(t.trim());
                }
            } else if (c.contains("sortby=")) {
                String sortBy = c.substring(7);
                if (sortBy.equals("weight")) {
                    ret.sortBy = "weight";
                } else {
                    ret.sortBy = "alphabetically";
                }
            } else if (c.contains("file=")) {
                String file = c.substring(5);
                ret.file = file;
            }
        }
        
        ret.tables = tables;
        
        return ret;
    }
    
    class ReportParameters {
        List<String> tables;
        String sortBy = "alphabetically";
        String file;
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("tables=").append(tables)
                    .append(", file=[").append(file).append("]")
                    .append(", sortBy=[").append(sortBy).append("]");
            
            return sb.toString();
        }
    }
}
