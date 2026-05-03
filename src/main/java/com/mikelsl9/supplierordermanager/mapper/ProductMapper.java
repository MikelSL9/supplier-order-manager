package com.mikelsl9.supplierordermanager.mapper;

import com.mikelsl9.supplierordermanager.dto.ProductRequest;
import com.mikelsl9.supplierordermanager.dto.ProductResponse;
import com.mikelsl9.supplierordermanager.entity.Product;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "supplier.id", target = "supplierId")
    ProductResponse toResponse(Product product);

    @Mapping(target = "id" , ignore = true)
    @Mapping(source = "productRequest.name", target = "name")
    Product toEntity(ProductRequest productRequest, Supplier supplier);
    List<ProductResponse> listEntityToResponse(List<Product> products);

}
