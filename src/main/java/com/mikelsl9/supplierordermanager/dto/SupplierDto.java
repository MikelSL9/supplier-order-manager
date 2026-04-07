package com.mikelsl9.supplierordermanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDto {
    private Long id;
    @NotBlank(message = "Supplier name is required")
    private String name;
    private String email;
    private String phoneNumber;
}
