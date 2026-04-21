package com.mikelsl9.supplierordermanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ImportResultDto {

    private int processedRows;
    private int createdProducts;
    private int updatedProducts;
    private int skippedRows;
    private List<String> rowErrors = new ArrayList<>();

}
