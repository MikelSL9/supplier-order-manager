package com.mikelsl9.supplierordermanager.dto.response;

public record SupplierResponse (
        Long id,
        String name,
        String email,
        String phoneNumber,
        Integer leadTimeDays
){}

