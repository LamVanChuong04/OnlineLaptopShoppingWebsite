package com.fia.project_ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fia.project_ecommerce.model.Product;
import com.fia.project_ecommerce.model.cart;
import com.fia.project_ecommerce.model.cartDetail;

public interface CartDetailRepository  extends JpaRepository<cartDetail, Long> {
    cartDetail findByCartAndProduct(cart cart, Product product);
    boolean existsByCartAndProduct(cart cart, Product product);
}
