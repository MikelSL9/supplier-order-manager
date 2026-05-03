package com.mikelsl9.supplierordermanager.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "suppliers")

public class Supplier{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="supplier_id")
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(length=120, nullable = false)
    private String name;
    @Column(length=75, unique = true)
    private String email;
    @Column(name="phone_number",length=12)
    private String phoneNumber;
    @Column(name="lead_time_days")
    private Integer leadTimeDays;

    public Supplier(String name, String email, String phoneNumber, Integer leadTimeDays) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.leadTimeDays = leadTimeDays;
    }
}
