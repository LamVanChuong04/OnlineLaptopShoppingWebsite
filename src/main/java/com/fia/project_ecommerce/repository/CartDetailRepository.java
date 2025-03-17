package com.fia.project_ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fia.project_ecommerce.model.cartDetail;

public interface CartDetailRepository  extends JpaRepository<cartDetail, Long> {
    
}
