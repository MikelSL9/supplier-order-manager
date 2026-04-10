package com.mikelsl9.supplierordermanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "suppliers")

public class Supplier{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="supplier_id")
    private Long id;
    @Column(length=120, nullable = false)
    private String name;
    @Column(length=75, unique = true)
    private String email;
    @Column(name="phone_number",length=12)
    private String phoneNumber;
    @Column(name="lead_time_days")
    private Integer leadTimeDays;

    protected Supplier() {
    }

    public Supplier(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}
