package com.mikelsl9.supplierordermanager.service;

import com.mikelsl9.supplierordermanager.dto.SupplierDto;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import com.mikelsl9.supplierordermanager.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<SupplierDto> findAll() {
        return supplierRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<SupplierDto> findById(Long id) {
        return supplierRepository.findById(id)
                .map(this::toDto);
    }

    public SupplierDto create(SupplierDto supplierDto) {
        Supplier supplier = toEntity(supplierDto);
        Supplier savedSupplier = supplierRepository.save(supplier);

        return toDto(savedSupplier);
    }

    public Optional<SupplierDto> update(Long id, SupplierDto supplierDto) {
        return supplierRepository.findById(id)
                .map(existingSupplier -> {
                    existingSupplier.setName(supplierDto.getName());
                    existingSupplier.setEmail(supplierDto.getEmail());
                    existingSupplier.setPhoneNumber(supplierDto.getPhoneNumber());

                    Supplier updatedSupplier = supplierRepository.save(existingSupplier);
                    return toDto(updatedSupplier);
                });
    }

    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

    private SupplierDto toDto(Supplier supplier) {
        SupplierDto dto = new SupplierDto();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setEmail(supplier.getEmail());
        dto.setPhoneNumber(supplier.getPhoneNumber());
        return dto;
    }

    private Supplier toEntity(SupplierDto supplierDto) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierDto.getName());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setPhoneNumber(supplierDto.getPhoneNumber());
        return supplier;
    }

}
