package com.mikelsl9.supplierordermanager.mapper;

import com.mikelsl9.supplierordermanager.dto.SupplierRequest;
import com.mikelsl9.supplierordermanager.dto.SupplierResponse;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierResponse toResponse(Supplier supplier);
    Supplier toEntity(SupplierRequest supplierRequest);
    List<SupplierResponse> listEntityToResponse(List<Supplier> suppliers);
    List<Supplier> listRequestToEntity(List<SupplierRequest> supplierRequests);

}
