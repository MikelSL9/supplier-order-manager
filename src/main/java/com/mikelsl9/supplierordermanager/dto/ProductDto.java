package com.mikelsl9.supplierordermanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Long id;
    private String barCode;
    private String name;
    private Integer currentStock;
    private String supplierRef;
    private Double dailySalesRate;
    private Integer totalSales;
    private Long supplierId;
}
