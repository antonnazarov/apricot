package za.co.apricotdb.support.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Component;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import za.co.apricotdb.support.excel.TableWrapper.ReportRow;

/**
 * This class physically writes the generated report on disk.
 *
 * @author Anton Nazarov
 * @since 06/10/2018
 */
@Component
public class ReportWriter {

    public void createReport(List<TableWrapper> tables, String fileName) throws Exception {
        Workbook wb = createWorkBook();
        Sheet sheet = wb.createSheet("DB Structure");
        createReportHeader(sheet);

        int rowNum = 1;
        for (TableWrapper t : tables) {
            rowNum++;
            createTableHeader(sheet, t.getTableName(), rowNum);
            rowNum++;
            List<ReportRow> rows = t.getRows();
            for (ReportRow r : rows) {
                createTableRow(sheet, r, rowNum);
                rowNum++;
            }
        }
        createEmptyRow(sheet, rowNum);

        autosizeSheet(sheet);
        writeToDisk(wb, fileName);
    }

    private void createEmptyRow(Sheet sheet, int rowNum) {
        Row r = sheet.createRow(rowNum);
        r.createCell(0, CellType.BLANK);
        r.createCell(1, CellType.BLANK);
        r.createCell(2, CellType.BLANK);
        r.createCell(3, CellType.BLANK);
        r.createCell(4, CellType.BLANK);
        r.createCell(5, CellType.BLANK);
    }

    private void createTableHeader(Sheet sheet, String tableName, int rowNum) {
        Row r = sheet.createRow(rowNum);
        Cell c = r.createCell(0);
        CellStyle style = createBoldCellStyle(sheet.getWorkbook());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        c.setCellStyle(style);
        c.setCellValue(tableName);

        c = r.createCell(1, CellType.BLANK);
        c.setCellStyle(style);
        c = r.createCell(2, CellType.BLANK);
        c.setCellStyle(style);
        c = r.createCell(3, CellType.BLANK);
        c.setCellStyle(style);
        c = r.createCell(4, CellType.BLANK);
        c.setCellStyle(style);
        c = r.createCell(5, CellType.BLANK);
        c.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
    }

    private void createTableRow(Sheet sheet, ReportRow row, int rowNum) {
        Row r = sheet.createRow(rowNum);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        Cell c = r.createCell(0);
        c.setCellValue(row.parentTable);
        c.setCellStyle(style);
        c = r.createCell(1);
        c.setCellValue(row.ordinalPosition);
        c.setCellStyle(style);
        c = r.createCell(2);
        c.setCellValue(row.columnName);
        c.setCellStyle(style);
        c = r.createCell(3);
        c.setCellValue(row.columnType);
        c.setCellStyle(style);
        c = r.createCell(4);
        c.setCellValue(row.constraints);
        c.setCellStyle(style);
        c = r.createCell(5);
        c.setCellValue(row.childTable);
        c.setCellStyle(style);
    }

    private Workbook createWorkBook() {
        Workbook wb = new XSSFWorkbook();

        return wb;
    }

    /**
     * Write the report on disk.
     */
    private void writeToDisk(Workbook workbook, String fileName) throws Exception {
        OutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
    }

    private void autosizeSheet(Sheet sheet) {
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
    }

    private void createReportHeader(Sheet sheet) {
        Row r = sheet.createRow(0);

        CellStyle style = createBoldCellStyle(sheet.getWorkbook());
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);

        Cell c = r.createCell(0);
        c.setCellValue("Parent tables");
        c.setCellStyle(style);
        c = r.createCell(1);
        c.setCellValue("#");
        c.setCellStyle(style);
        c = r.createCell(2);
        c.setCellValue("Field name");
        c.setCellStyle(style);
        c = r.createCell(3);
        c.setCellValue("Type");
        c.setCellStyle(style);
        c = r.createCell(4);
        c.setCellValue("Constraint");
        c.setCellStyle(style);
        c = r.createCell(5);
        c.setCellValue("Child tables");
        c.setCellStyle(style);
    }

    private CellStyle createBoldCellStyle(Workbook wb) {
        Font font = wb.createFont();
        font.setBold(true);
        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        return style;
    }
}
