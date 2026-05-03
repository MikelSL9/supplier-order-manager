package com.mikelsl9.supplierordermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SupplierResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        Integer leadTimeDays
) {}
