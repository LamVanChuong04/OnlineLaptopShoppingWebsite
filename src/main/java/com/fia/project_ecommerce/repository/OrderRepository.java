package com.fia.project_ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fia.project_ecommerce.model.Order;

public interface OrderRepository  extends JpaRepository<Order, Long>{
    
}
