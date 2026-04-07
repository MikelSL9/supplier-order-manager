package com.mikelsl9.supplierordermanager.util;

import com.mikelsl9.supplierordermanager.dto.SalesRowDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelReaderComponent {

    private static final int HEADER_ROWS_TO_SKIP = 4;


    public List<SalesRowDto> parseSalesExcel(MultipartFile multipartFile){
        List<SalesRowDto> resultList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (InputStream is = multipartFile.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

                 Sheet sheet = workbook.getSheetAt(0);
                 int lastRowIndex = sheet.getLastRowNum();

                 for (int i = HEADER_ROWS_TO_SKIP; i < lastRowIndex; i++) {
                     Row row = sheet.getRow(i);
                     if (row == null) continue;

                     String barCode = getStringCell(row, 0, formatter);
                     String name = getStringCell(row, 1, formatter);
                     Integer quantity = getIntegerCell(row, 2, formatter);

                     // Solo añadimos si hay algún dato útil
                     if ((barCode != null && !barCode.isBlank()) || (name != null && !name.isBlank())) {
                         resultList.add(new SalesRowDto(barCode, name, quantity));
                     }
                 }
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not read sales .xlsx file", e);
        }

        return resultList;
    }

    private String getStringCell(Row row, int columnIndex, DataFormatter formatter) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;
        String value = formatter.formatCellValue(cell);
        return value == null ? null : value.trim();
    }

    private Integer getIntegerCell(Row row, int columnIndex, DataFormatter formatter) {
        String value = getStringCell(row, columnIndex, formatter);
        if (value == null || value.isBlank()) return null;
        try {
            return (int) Math.round(Double.parseDouble(value.replace(",", ".")));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
