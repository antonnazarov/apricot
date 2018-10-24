package za.co.apricotdb.support.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
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
        Sheet tablesSheet = wb.createSheet("Tables List");
        Map<String, String> tableHyperlinks = new LinkedHashMap<>();
        Sheet sheet = wb.createSheet("Table Details");
        createReportHeader(sheet);

        int rowNum = 1;
        for (TableWrapper t : tables) {
            rowNum++;
            createTableHeader(sheet, t.getTableName(), rowNum);
            rowNum++;
            tableHyperlinks.put(t.getTableName(), "'Table Details'!A" + rowNum);

            List<ReportRow> rows = t.getRows();
            for (ReportRow r : rows) {
                if (r == rows.get(rows.size() - 1)) {
                    //  the last row in the table
                    createTableRow(sheet, r, rowNum, getDoubleBottomBorderStyle(sheet));
                } else {
                    createTableRow(sheet, r, rowNum, getThinBorderStyle(sheet));
                }
                rowNum++;
            }

            rowNum = createConstraintsLegend(sheet, t, rowNum);
        }

        createListOfContext(tablesSheet, tableHyperlinks);
        makeHyperlinks(sheet, tableHyperlinks, rowNum);

        autosizeSheet(tablesSheet);
        autosizeSheet(sheet);

        writeToDisk(wb, fileName);
    }

    private void makeHyperlinks(Sheet sheet, Map<String, String> tableHyperlinks, int rowsTotal) {
        CreationHelper createHelper = sheet.getWorkbook().getCreationHelper();
        for (int i = 1; i <= rowsTotal; i++) {
            Row r = sheet.getRow(i);
            if (r != null && r.getCell(1) != null) {
                Cell c = r.getCell(1);
                CellType t = c.getCellType();
                if (t == CellType.NUMERIC || t == CellType.STRING) {
                    if (r.getCell(0) != null) {
                        handleHyperCell(r.getCell(0), tableHyperlinks, createHelper);
                    }
                    if (r.getCell(5) != null) {
                        handleHyperCell(r.getCell(5), tableHyperlinks, createHelper);
                    }
                }
            }
        }
    }

    private void handleHyperCell(Cell c, Map<String, String> tableHyperlinks, CreationHelper createHelper) {
        String value = c.getStringCellValue();
        if (value != null && value.length() > 0) {
            String address = tableHyperlinks.get(value);
            if (address != null) {
                CellStyle hlinkStyle = c.getRow().getSheet().getWorkbook().createCellStyle();
                hlinkStyle.cloneStyleFrom(c.getCellStyle());
                hlinkStyle = createHyperlinkStyle(c.getRow().getSheet().getWorkbook(), hlinkStyle);
                c.setCellStyle(hlinkStyle);
                Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
                link.setAddress(address);
                c.setHyperlink(link);
            }
        }
    }

    private void createListOfContext(Sheet tablesSheet, Map<String, String> tableHyperlinks) {
        CreationHelper createHelper = tablesSheet.getWorkbook().getCreationHelper();
        CellStyle hlinkStyle = createHyperlinkStyle(tablesSheet.getWorkbook(), null);
        int rowNum = 0;
        for (Entry<String, String> e : tableHyperlinks.entrySet()) {
            Row row = tablesSheet.createRow(rowNum);
            Cell c = row.createCell(0);
            c.setCellStyle(hlinkStyle);
            c.setCellValue(e.getKey());
            Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
            link.setAddress(e.getValue());
            c.setHyperlink(link);

            rowNum++;
        }
    }

    private CellStyle createHyperlinkStyle(Workbook wb, CellStyle style) {
        CellStyle hlinkStyle = style;
        if (style == null) {
            hlinkStyle = wb.createCellStyle();
        }
        Font hlinkFont = wb.createFont();
        hlinkFont.setUnderline(Font.U_SINGLE);
        hlinkFont.setColor(IndexedColors.BLUE.getIndex());
        hlinkStyle.setFont(hlinkFont);

        return hlinkStyle;
    }

    private void createTableHeader(Sheet sheet, String tableName, int rowNum) {
        Row r = sheet.createRow(rowNum);
        Cell c = r.createCell(0);
        CellStyle style = getThickBorderStyle(sheet);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        makeBoldCellStyle(sheet.getWorkbook(), style);

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

    private Row createTableRow(Sheet sheet, ReportRow row, int rowNum, CellStyle style) {
        Row r = sheet.createRow(rowNum);

        if (style == null) {
            style = getThinBorderStyle(sheet);
        }

        Cell c = r.createCell(0);
        c.setCellValue(row.parentTable);
        c.setCellStyle(style);
        
        c = r.createCell(1);
        if (row.ordinalPosition != 0) {
            c.setCellValue(row.ordinalPosition);
        } else {
            c.setCellValue(" -- ");
        }
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
        
        return r;
    }

    private CellStyle getThinBorderStyle(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle getDoubleBottomBorderStyle(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.DOUBLE);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle getDoubleTopBorderStyle(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();

        style.setBorderTop(BorderStyle.DOUBLE);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle getDoubleTopAndBottomBorderStyle(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();

        style.setBorderTop(BorderStyle.DOUBLE);
        style.setBorderBottom(BorderStyle.DOUBLE);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle getThickBorderStyle(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();

        style.setBorderTop(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);

        return style;
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

        CellStyle style = getThickBorderStyle(sheet);
        makeBoldCellStyle(sheet.getWorkbook(), style);

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

    private void makeBoldCellStyle(Workbook wb, CellStyle style) {
        Font font = wb.createFont();
        font.setBold(true);

        style.setFont(font);
    }

    private void makeItalicCellStyle(Workbook wb, CellStyle style) {
        Font font = wb.createFont();
        font.setItalic(true);

        style.setFont(font);
    }

    private int createConstraintsLegend(Sheet sheet, TableWrapper table, int rowNum) {
        int currRowNum = rowNum;
        Map<String, String> l = table.getConstraintsLegend();

        if (l.size() == 0) {
            return rowNum;
        }

        Set<Entry<String, String>> legends = l.entrySet();

        CellStyle style = getDoubleTopBorderStyle(sheet);
        if (legends.size() == 1) {
            style = getDoubleTopAndBottomBorderStyle(sheet);
        }
        boolean first = true;
        String lastKey = l.keySet().toArray(new String[l.size()])[l.size() - 1];

        for (Entry<String, String> e : legends) {

            if (!first && legends.size() > 1) {
                if (!e.getKey().equals(lastKey)) {
                    style = getThinBorderStyle(sheet);
                } else {
                    style = getDoubleBottomBorderStyle(sheet);
                }
            }
            createConstraintsLegendRow(sheet, e, style, currRowNum);
            currRowNum++;
            first = false;
        }

        return currRowNum;
    }

    private void createConstraintsLegendRow(Sheet sheet, Entry<String, String> entry, CellStyle style, int rowNum) {
        makeItalicCellStyle(sheet.getWorkbook(), style);

        Row r = sheet.createRow(rowNum);
        r.createCell(0, CellType.BLANK);
        r.createCell(1, CellType.BLANK);
        r.createCell(2, CellType.BLANK);
        r.createCell(3, CellType.BLANK);

        Cell c = r.createCell(4);
        c.setCellValue(entry.getKey());
        c.setCellStyle(style);
        c = r.createCell(5);
        c.setCellValue(entry.getValue());
        c.setCellStyle(style);
    }
}
