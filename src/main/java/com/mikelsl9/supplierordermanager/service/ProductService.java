package com.mikelsl9.supplierordermanager.service;

import com.mikelsl9.supplierordermanager.dto.ProductDto;
import com.mikelsl9.supplierordermanager.entity.Product;
import com.mikelsl9.supplierordermanager.entity.Supplier;
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

    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id)
                .map(this::toDto);
    }

    public ProductDto create(ProductDto productDto) {
        validateProduct(productDto);
        Product product = toEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return toDto(savedProduct);
    }

    public Optional<ProductDto> update(Long id, ProductDto productDto) {
        validateProduct(productDto);
        return productRepository.findById(id)

                .map(existingProduct -> {
                    Supplier supplier = getSupplierOrThrow(productDto.getSupplierId());
                    existingProduct.setBarCode(productDto.getBarCode());
                    existingProduct.setName(productDto.getName());
                    existingProduct.setCurrentStock(productDto.getCurrentStock());
                    existingProduct.setSupplierRef(productDto.getSupplierRef());
                    existingProduct.setDailySalesRate(productDto.getDailySalesRate());
                    existingProduct.setTotalSales(productDto.getTotalSales());
                    existingProduct.setSupplier(supplier);
                    Product updatedProduct = productRepository.save(existingProduct);

                    return toDto(updatedProduct);

                });
    }



    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductDto> findBySupplierId(Long supplierId) {
        return productRepository.findBySupplierId(supplierId).stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<ProductDto> findByBarCode(String barCode) {
        return productRepository.findByBarCode(barCode)
                .map(this::toDto);

    }

    public List<ProductDto> findReorderCandidates(Integer coverageDays) {
        int days = coverageDays == null ? DEFAULT_REORDER_COVERAGE_DAYS : coverageDays;
        if (days < 1) {
            throw new IllegalArgumentException("coverageDays must be greater than 0");
        }

        return productRepository.findAll().stream()
                .filter(product -> needsReorder(product, days))
                .map(this::toDto)
                .toList();

    }

    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setBarCode(product.getBarCode());
        dto.setName(product.getName());
        dto.setCurrentStock(product.getCurrentStock());
        dto.setSupplierRef(product.getSupplierRef());
        dto.setDailySalesRate(product.getDailySalesRate());
        dto.setTotalSales(product.getTotalSales());
        dto.setSupplierId(product.getSupplier().getId());
        return dto;

    }

    private Product toEntity(ProductDto productDto) {
        Supplier supplier = getSupplierOrThrow(productDto.getSupplierId());
        Product product = new Product();
        product.setBarCode(productDto.getBarCode());
        product.setName(productDto.getName());
        product.setCurrentStock(productDto.getCurrentStock());
        product.setSupplierRef(productDto.getSupplierRef());
        product.setDailySalesRate(productDto.getDailySalesRate());
        product.setTotalSales(productDto.getTotalSales());
        product.setSupplier(supplier);
        return product;
    }


    private Supplier getSupplierOrThrow(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found: " + supplierId));
    }

    private void validateProduct(ProductDto productDto) {

        if (productDto.getBarCode() == null || productDto.getBarCode().isBlank()) {
            throw new IllegalArgumentException("Product barCode is required");
        }
        if (productDto.getName() == null || productDto.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (productDto.getCurrentStock() == null || productDto.getCurrentStock() < 0) {
            throw new IllegalArgumentException("Product currentStock must be 0 or greater");
        }
        if (productDto.getSupplierRef() == null || productDto.getSupplierRef().isBlank()) {
            throw new IllegalArgumentException("Product supplierRef is required");
        }
        if (productDto.getSupplierId() == null) {
            throw new IllegalArgumentException("Product supplierId is required");
        }

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

