package com.automation.utils;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;

public class ExcelReader {

    private Workbook workbook;

    public ExcelReader(String filePath) {
        // try-with-resources — the stream is only needed to construct
        // the Workbook; closing it immediately after avoids leaking
        // file handles, which on Windows can lock the file for any
        // subsequent ExcelReader instance pointed at the same path.
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            workbook = WorkbookFactory.create(fis);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to open excel file",
                    e
            );
        }
    }

    public Sheet getSheet(String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public int getRowCount(String sheetName) {
        return getSheet(sheetName).getPhysicalNumberOfRows();
    }

    public String getCellData(
            String sheetName,
            int row,
            int column
    ) {
        Cell cell = getSheet(sheetName)
                .getRow(row)
                .getCell(column);

        if (cell == null) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(cell);
    }
}