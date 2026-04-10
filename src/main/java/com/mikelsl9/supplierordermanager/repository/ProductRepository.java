package com.mikelsl9.supplierordermanager.repository;

import com.mikelsl9.supplierordermanager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySupplierId(Long id);
    Optional<Product> findByBarCode(String barCode);
}
