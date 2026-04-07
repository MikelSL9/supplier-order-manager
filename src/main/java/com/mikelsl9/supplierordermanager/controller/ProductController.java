package com.mikelsl9.supplierordermanager.controller;

import com.mikelsl9.supplierordermanager.dto.ProductDto;
import com.mikelsl9.supplierordermanager.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.update(id, productDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity<List<ProductDto>> findBySupplierId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findBySupplierId(id));
    }

    @GetMapping("/barCode/{barCode}")
    public ResponseEntity<ProductDto> findByBarCode(@PathVariable String barCode) {
        return productService.findByBarCode(barCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reorder-candidates")
    public ResponseEntity<List<ProductDto>> findReorderCandidates(@RequestParam(required = false) Integer coverageDays) {
        return ResponseEntity.ok(productService.findReorderCandidates(coverageDays));
    }
}
