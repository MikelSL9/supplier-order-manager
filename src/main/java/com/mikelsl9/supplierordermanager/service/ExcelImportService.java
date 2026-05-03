/*package com.mikelsl9.supplierordermanager.service;

import com.mikelsl9.supplierordermanager.dto.ImportResultDto;
import com.mikelsl9.supplierordermanager.entity.Product;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import com.mikelsl9.supplierordermanager.repository.ProductRepository;
import com.mikelsl9.supplierordermanager.repository.SupplierRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExcelImportService {

    private static final int HEADER_ROWS_TO_SKIP = 4;
    private static final int DEFAULT_DAILY_WINDOW_DAYS = 30;

    private static final int STOCK_COL_BARCODE = 0;
    private static final int STOCK_COL_NAME = 1;
    private static final int STOCK_COL_CURRENT_STOCK = 2;
    private static final int STOCK_COL_SUPPLIER_NAME = 16;
    private static final int STOCK_COL_SUPPLIER_REF = 17;

    private static final int SALES_COL_BARCODE = 0;
    private static final int SALES_COL_NAME = 1;
    private static final int SALES_COL_QUANTITY = 2;

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public ExcelImportService(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public ImportResultDto importProductsStock(MultipartFile file) {
        validateXlsxFile(file);

        ImportResultDto result = new ImportResultDto();
        DataFormatter formatter = new DataFormatter();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowIndex = sheet.getLastRowNum();

            for (int rowIndex = HEADER_ROWS_TO_SKIP; rowIndex < lastRowIndex; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    result.setSkippedRows(result.getSkippedRows() + 1);
                    continue;
                }

                String barCode = getStringCell(row, STOCK_COL_BARCODE, formatter);
                String name = getStringCell(row, STOCK_COL_NAME, formatter);
                Integer currentStock = getIntegerCell(row, STOCK_COL_CURRENT_STOCK, formatter);
                String supplierName = getStringCell(row, STOCK_COL_SUPPLIER_NAME, formatter);
                String supplierRef = getStringCell(row, STOCK_COL_SUPPLIER_REF, formatter);

                if (barCode == null || barCode.isBlank() || name == null || name.isBlank()
                        || supplierName == null || supplierName.isBlank()) {
                    result.setSkippedRows(result.getSkippedRows() + 1);
                    continue;
                }

                Supplier supplier = supplierRepository.findByNameIgnoreCase(supplierName.trim())
                        .orElseGet(() -> supplierRepository.save(new Supplier(supplierName.trim(), null, null)));

                Product product = productRepository.findByBarCode(barCode.trim()).orElse(null);
                if (product == null) {
                    product = new Product();
                    product.setBarCode(barCode.trim());
                    result.setCreatedProducts(result.getCreatedProducts() + 1);
                } else {
                    result.setUpdatedProducts(result.getUpdatedProducts() + 1);
                }

                product.setName(name);
                product.setCurrentStock(currentStock == null ? 0 : currentStock);
                product.setSupplierRef(supplierRef == null || supplierRef.isBlank() ? "N/A" : supplierRef.trim());
                product.setSupplier(supplier);

                productRepository.save(product);
                result.setProcessedRows(result.getProcessedRows() + 1);
            }

            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read .xlsx file", e);
        }
    }

    @Transactional
    public ImportResultDto importProductsSales(MultipartFile file) {
        validateXlsxFile(file);
        ImportResultDto result = new ImportResultDto();

        DataFormatter formatter = new DataFormatter();
        Map<String, Product> productsByNormalizedName = buildProductsByNormalizedName();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastRowIndex = sheet.getLastRowNum();

            for (int rowIndex = HEADER_ROWS_TO_SKIP; rowIndex < lastRowIndex; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    result.setSkippedRows(result.getSkippedRows() + 1);
                    continue;
                }

                String barCode = getStringCell(row, SALES_COL_BARCODE, formatter);
                String name = getStringCell(row, SALES_COL_NAME, formatter);
                Integer quantity = getIntegerCell(row, SALES_COL_QUANTITY, formatter);

                if ((barCode == null || barCode.isBlank()) && (name == null || name.isBlank())) {
                    result.setSkippedRows(result.getSkippedRows() + 1);
                    result.getUnreconciledRows().add("Row " + (rowIndex + 1) + ": missing barCode and name");
                    continue;
                }

                Product product = findProductForSalesRow(barCode, name, productsByNormalizedName);
                if (product == null) {
                    result.setSkippedRows(result.getSkippedRows() + 1);
                    result.getUnreconciledRows().add("Row " + (rowIndex + 1) + ": could not match product (barCode="
                            + safeLabel(barCode) + ", name=" + safeLabel(name) + ")");
                    continue;
                }

                if (name != null && !name.isBlank()) {
                    product.setName(name);
                }
                int totalSales = quantity == null ? 0 : quantity;
                product.setTotalSales(totalSales);
                product.setDailySalesRate(calculateDailySalesRate(totalSales));
                productRepository.save(product);

                result.setUpdatedProducts(result.getUpdatedProducts() + 1);
                result.setProcessedRows(result.getProcessedRows() + 1);
            }

            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read .xlsx file", e);
        }
    }


    private void validateXlsxFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        String name = file.getOriginalFilename();
        if (name == null || !name.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Only .xlsx files are supported");
        }
    }

    private String getStringCell(Row row, int columnIndex, DataFormatter formatter) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        String value = formatter.formatCellValue(cell);
        return value == null ? null : value.trim();
    }

    private Integer getIntegerCell(Row row, int columnIndex, DataFormatter formatter) {
        String value = getStringCell(row, columnIndex, formatter);
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return (int) Math.round(Double.parseDouble(value.replace(",", ".")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Product findProductForSalesRow(String barCode, String name, Map<String, Product> productsByNormalizedName) {
        if (barCode != null && !barCode.isBlank()) {
            Product productByBarCode = productRepository.findByBarCode(barCode.trim()).orElse(null);
            if (productByBarCode != null) {
                return productByBarCode;
            }
        }

        if (name == null || name.isBlank()) {
            return null;
        }

        return productsByNormalizedName.get(normalizeText(name));
    }

    private Map<String, Product> buildProductsByNormalizedName() {
        Map<String, Product> byName = new HashMap<>();
        for (Product product : productRepository.findAll()) {
            if (product.getName() == null || product.getName().isBlank()) {
                continue;
            }
            byName.putIfAbsent(normalizeText(product.getName()), product);
        }
        return byName;
    }

    private String normalizeText(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
        return normalized.replaceAll("[^a-z0-9]", "");
    }

    private double calculateDailySalesRate(int totalSales) {
        return (double) totalSales / DEFAULT_DAILY_WINDOW_DAYS;
    }

    private String safeLabel(String value) {
        if (value == null || value.isBlank()) {
            return "N/A";
        }
        return value.trim();
    }
}
*/