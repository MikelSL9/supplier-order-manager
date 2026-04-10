package com.mikelsl9.supplierordermanager.service;

import com.mikelsl9.supplierordermanager.dto.ProductDto;
import com.mikelsl9.supplierordermanager.entity.Product;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import com.mikelsl9.supplierordermanager.mapper.ProductMapper;
import com.mikelsl9.supplierordermanager.exception.ResourceNotFoundException;
import com.mikelsl9.supplierordermanager.repository.ProductRepository;
import com.mikelsl9.supplierordermanager.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final int DEFAULT_REORDER_COVERAGE_DAYS = 7;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return productMapper.toDto(product);
    }

    public ProductDto create(ProductDto productDto) {
        Supplier supplier = supplierRepository.findById(productDto.supplierId()).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", productDto.supplierId()));

        Product product = productMapper.toEntity(productDto, supplier);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);

    }

    public ProductDto update(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Product", id));

        Supplier supplierDto = supplierRepository.findById(productDto.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", productDto.supplierId()));

        productMapper.updateEntityFromDto(productDto, existingProduct, supplierDto);

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(updatedProduct);
    }


    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDto> findBySupplierId(Long supplierId) {
        supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));
        return productRepository.findBySupplierId(supplierId).stream()
                .map(productMapper::toDto)
                .toList();
    }


    public List<ProductDto> findReorderCandidates(Integer coverageDays) {
        int days = coverageDays == null ? DEFAULT_REORDER_COVERAGE_DAYS : coverageDays;
        if (days < 1) {
            throw new IllegalArgumentException("coverageDays must be greater than 0");
        }

        return productRepository.findAll().stream()
                .filter(product -> needsReorder(product, days))
                .map(productMapper::toDto)
                .toList();

    }

    private boolean needsReorder(Product product, int coverageDays) {

        double dailySalesRate = product.getDailySalesRate() == null ? 0.0 : product.getDailySalesRate();
        if (dailySalesRate <= 0) {
            return false;
        }

        int currentStock = product.getCurrentStock() == null ? 0 : product.getCurrentStock();
        int minimumRequiredStock = (int) Math.ceil(dailySalesRate * coverageDays);

        return currentStock < minimumRequiredStock;

    }

}

