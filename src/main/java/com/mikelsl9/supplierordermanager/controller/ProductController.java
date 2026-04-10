package com.mikelsl9.supplierordermanager.controller;

import com.mikelsl9.supplierordermanager.dto.ProductDto;
import com.mikelsl9.supplierordermanager.dto.SupplierDto;
import com.mikelsl9.supplierordermanager.service.ProductService;
import jakarta.validation.Valid;
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
       ProductDto product = productService.findById(id);
       return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(productDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid  @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.update(id, productDto);
        return ResponseEntity.ok(updatedProduct);
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


    @GetMapping("/reorder-candidates")
    public ResponseEntity<List<ProductDto>> findReorderCandidates(@RequestParam(required = false) Integer coverageDays) {
        return ResponseEntity.ok(productService.findReorderCandidates(coverageDays));
    }
}
