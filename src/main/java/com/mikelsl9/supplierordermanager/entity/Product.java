package com.mikelsl9.supplierordermanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "bar_code", length = 13, nullable = false, unique = true)
    private String barCode;

    @Column(length = 120, nullable = false)
    private String name;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "supplier_ref", length = 60, nullable = false)
    private String supplierRef;

    @Column(name = "daily_sales_rate")
    private Double dailySalesRate;

    @Column(name = "total_sales")
    private Integer totalSales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    public Product() {
    }

    public Product(String barCode, String name, Integer currentStock, String supplierRef, Double dailySalesRate, Integer totalSales, Supplier supplier) {
        this.barCode = barCode;
        this.name = name;
        this.currentStock = currentStock;
        this.supplierRef = supplierRef;
        this.dailySalesRate = dailySalesRate;
        this.totalSales = totalSales;
        this.supplier = supplier;
    }

}

