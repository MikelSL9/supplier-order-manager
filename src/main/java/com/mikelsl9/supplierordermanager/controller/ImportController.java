package com.mikelsl9.supplierordermanager.controller;

import com.mikelsl9.supplierordermanager.dto.ImportResultDto;
import com.mikelsl9.supplierordermanager.service.ExcelImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final ExcelImportService excelImportService;

    public ImportController(ExcelImportService excelImportService) {
        this.excelImportService = excelImportService;
    }

    @PostMapping("/products-stock")
    public ResponseEntity<ImportResultDto> importProductsStock(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(excelImportService.importProductsStock(file));
    }

    @PostMapping("/products-sales")
    public ResponseEntity<ImportResultDto> importProductsSales(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(excelImportService.importProductsSales(file));
    }
}
