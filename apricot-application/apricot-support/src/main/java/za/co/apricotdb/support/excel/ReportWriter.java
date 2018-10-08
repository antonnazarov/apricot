package za.co.apricotdb.support.excel;

import org.springframework.stereotype.Component;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class physically writes the generated report on disk.
 * 
 * @author Anton Nazarov
 * @since 06/10/2018
 */
@Component
public class ReportWriter {
    
    /**
     * Write the report on disk.
     */
    public void writeToDisk(HSSFWorkbook workbook, String fileName) {
        
    }

}
