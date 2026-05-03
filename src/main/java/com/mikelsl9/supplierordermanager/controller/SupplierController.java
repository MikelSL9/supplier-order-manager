package com.mikelsl9.supplierordermanager.controller;

import com.mikelsl9.supplierordermanager.dto.SupplierRequest;
import com.mikelsl9.supplierordermanager.dto.SupplierResponse;
import com.mikelsl9.supplierordermanager.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public List<SupplierResponse> findAll() {
        return supplierService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> findById(@PathVariable Long id) {
        SupplierResponse supplier = supplierService.findById(id);
        return ResponseEntity.ok(supplier);

    }

    @PostMapping
    public ResponseEntity<SupplierResponse> create(@Valid @RequestBody SupplierRequest supplierRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supplierService.create(supplierRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> update(@PathVariable Long id, @Valid @RequestBody SupplierRequest supplierRequest) {
        SupplierResponse updatedSupplier = supplierService.update(id, supplierRequest);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        supplierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
