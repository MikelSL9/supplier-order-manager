package com.mikelsl9.supplierordermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(
        @NotBlank(message = "Supplier name is required")
        String name,
        String email,
        String phoneNumber,
        @Min(value = 0, message = "Lead time cannot be negative")
        Integer leadTimeDays
) {}
