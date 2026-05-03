package com.mikelsl9.supplierordermanager.controller;

import com.mikelsl9.supplierordermanager.dto.ProductRequest;
import com.mikelsl9.supplierordermanager.dto.ProductResponse;
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
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
       return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid  @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.update(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity<List<ProductResponse>> findBySupplierId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findBySupplierId(id));
    }


    /*@GetMapping("/reorder-candidates")
    public ResponseEntity<List<ProductDto>> findReorderCandidates(@RequestParam(required = false) Integer coverageDays) {
        return ResponseEntity.ok(productService.findReorderCandidates(coverageDays));
    }*/
}
