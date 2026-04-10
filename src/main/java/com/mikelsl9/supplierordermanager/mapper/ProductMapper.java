package com.mikelsl9.supplierordermanager.mapper;

import com.mikelsl9.supplierordermanager.dto.ProductDto;
import com.mikelsl9.supplierordermanager.entity.Product;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getBarCode(),
                product.getName(),
                product.getCurrentStock(),
                product.getSupplierRef(),
                product.getDailySalesRate(),
                product.getTotalSales(),
                product.getSupplier().getId()
        );
    }

    public Product toEntity(ProductDto productDto, Supplier supplier) {
        return new Product(
                productDto.barCode(),
                productDto.name(),
                productDto.currentStock(),
                productDto.supplierRef(),
                productDto.dailySalesRate(),
                productDto.totalSales(),
                supplier
        );
    }

    public void updateEntityFromDto(ProductDto dto, Product existingProduct, Supplier supplier) {
        existingProduct.setBarCode(dto.barCode());
        existingProduct.setName(dto.name());
        existingProduct.setCurrentStock(dto.currentStock());
        existingProduct.setSupplierRef(dto.supplierRef());
        existingProduct.setDailySalesRate(dto.dailySalesRate());
        existingProduct.setTotalSales(dto.totalSales());
        existingProduct.setSupplier(supplier);
    }

}
