package com.mikelsl9.supplierordermanager.service;

import com.mikelsl9.supplierordermanager.dto.ProductRequest;
import com.mikelsl9.supplierordermanager.dto.ProductResponse;
import com.mikelsl9.supplierordermanager.entity.Product;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import com.mikelsl9.supplierordermanager.mapper.ProductMapper;
import com.mikelsl9.supplierordermanager.exception.ResourceNotFoundException;
import com.mikelsl9.supplierordermanager.repository.ProductRepository;
import com.mikelsl9.supplierordermanager.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    //private static final int DEFAULT_REORDER_COVERAGE_DAYS = 7;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> findAll() {
        /*return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
         */
        List<Product> products = productRepository.findAll();
        return productMapper.listEntityToResponse(products);

    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return productMapper.toResponse(product);
    }

    public ProductResponse create(ProductRequest productRequest) {
        Supplier supplier = supplierRepository.findById(productRequest.supplierId()).orElseThrow(
                () -> new ResourceNotFoundException("Supplier", productRequest.supplierId()));

        Product product = productMapper.toEntity(productRequest, supplier);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);

    }

    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Product", id));

        Supplier supplier = supplierRepository.findById(productRequest.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", productRequest.supplierId()));

        existingProduct.setBarCode(productRequest.barCode());
        existingProduct.setName(productRequest.name());
        existingProduct.setCurrentStock(productRequest.currentStock());
        existingProduct.setSupplierRef(productRequest.supplierRef());
        existingProduct.setSupplier(supplier);

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(updatedProduct);
    }


    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponse> findBySupplierId(Long supplierId) {

        supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

        List<Product> products = productRepository.findBySupplierId(supplierId);
        return productMapper.listEntityToResponse(products);
    }


    /*public List<ProductDto> findReorderCandidates(Integer coverageDays) {
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

    }*/

}

