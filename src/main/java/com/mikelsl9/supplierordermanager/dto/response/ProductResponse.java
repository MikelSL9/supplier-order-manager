package com.mikelsl9.supplierordermanager.dto.response;

public record ProductResponse(
        Long id,
        String barCode,
        String name,
        Integer currentStock,
        String supplierRef,
        Double dailySalesRate,
        Integer totalSales,
        Long supplierId
) {
}
