package com.fia.project_ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fia.project_ecommerce.model.User;
import com.fia.project_ecommerce.model.cart;

@Repository
public interface CartRepository extends JpaRepository<cart, Long> {

    cart findByUser(User user);
} 
