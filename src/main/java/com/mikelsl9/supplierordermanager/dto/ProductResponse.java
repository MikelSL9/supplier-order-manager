package com.mikelsl9.supplierordermanager.dto;

public record ProductResponse(
    Long id,
    String barCode,
    String name,
    Integer currentStock,
    String supplierRef,
    Long supplierId
) {}
