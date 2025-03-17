package com.fia.project_ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fia.project_ecommerce.model.Product;

public interface  ProductRepository extends JpaRepository<Product, Long>{
    
}
