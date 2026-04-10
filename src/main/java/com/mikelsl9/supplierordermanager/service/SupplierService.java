package com.mikelsl9.supplierordermanager.service;

import com.mikelsl9.supplierordermanager.dto.SupplierDto;
import com.mikelsl9.supplierordermanager.entity.Supplier;
import com.mikelsl9.supplierordermanager.exception.ResourceNotFoundException;
import com.mikelsl9.supplierordermanager.mapper.SupplierMapper;
import com.mikelsl9.supplierordermanager.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    public List<SupplierDto> findAll() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toDto)
                .toList();
    }

    public SupplierDto findById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier",id));
        return supplierMapper.toDto(supplier);
    }

    public SupplierDto create(SupplierDto supplierDto) {
        Supplier supplier = supplierMapper.toEntity(supplierDto);
        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toDto(savedSupplier);
    }

    public SupplierDto update(Long id, SupplierDto supplierDto) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier",id));

        supplierMapper.updateEntityFromDto(supplierDto, existingSupplier);
        Supplier updatedSupplier = supplierRepository.save(existingSupplier);

        return supplierMapper.toDto(updatedSupplier);
    }

    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

}
