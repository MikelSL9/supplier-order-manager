package com.mikelsl9.supplierordermanager.service;

import com.mikelsl9.supplierordermanager.dto.SupplierRequest;
import com.mikelsl9.supplierordermanager.dto.SupplierResponse;
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

    public List<SupplierResponse> findAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return supplierMapper.listEntityToResponse(suppliers);
    }

    /*public List<SupplierResponse> findAll() {
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponse)
                .toList();
    }*/

    public SupplierResponse findById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier",id));
        return supplierMapper.toResponse(supplier);
    }

    public SupplierResponse create(SupplierRequest supplierRequest) {
        Supplier supplier = supplierMapper.toEntity(supplierRequest);
        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toResponse(savedSupplier);
    }

    public SupplierResponse update(Long id, SupplierRequest supplierRequest) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier",id));

        existingSupplier.setName(supplierRequest.name());
        existingSupplier.setEmail(supplierRequest.email());
        existingSupplier.setPhoneNumber(supplierRequest.phoneNumber());
        existingSupplier.setLeadTimeDays(supplierRequest.leadTimeDays());

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);

        return supplierMapper.toResponse(updatedSupplier);
    }

    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

}
