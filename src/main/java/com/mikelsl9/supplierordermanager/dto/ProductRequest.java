package com.mikelsl9.supplierordermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
    @NotBlank(message = "Product barcode is required")
    String barCode,
    @NotBlank(message = "Product name is required")
    String name,
    @NotNull(message = "Current Stock is required")
    @Min(value = 0, message = "Product currentStock must be 0 or greater")
    Integer currentStock,
    @NotBlank(message = "Supplier reference is required")
    String supplierRef,
    @NotNull(message = "Supplier ID is required")
    Long supplierId
) {}
