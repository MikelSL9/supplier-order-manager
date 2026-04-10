package com.mikelsl9.supplierordermanager.mapper;

import com.mikelsl9.supplierordermanager.dto.SupplierDto;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierDto toDto(Supplier supplier) {
        return new SupplierDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhoneNumber(),
                supplier.getLeadTimeDays()
        );
    }

    public Supplier toEntity(SupplierDto supplierDto) {
        return new Supplier(
                supplierDto.name(),
                supplierDto.email(),
                supplierDto.phoneNumber()
        );
    }

    public void updateEntityFromDto(SupplierDto dto, Supplier supplier) {
        supplier.setName(dto.name());
        supplier.setEmail(dto.email());
        supplier.setPhoneNumber(dto.phoneNumber());
        supplier.setLeadTimeDays(dto.leadTimeDays());
    }
}
