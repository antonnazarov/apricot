package za.co.apricotdb.support.util;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.DataReader;
import za.co.apricotdb.persistence.data.MetaData;

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

                System.out.println(result);
            }
        }
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
            }
        }
        
        ret.tables = tables;
        
        return ret;
    }
    
    class ReportParameters {
        List<String> tables;
        String sortBy = "alphabetically";
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("tables=").append(tables).append(", sortBy=[").append(sortBy).append("]");
            
            return sb.toString();
        }
    }
}
