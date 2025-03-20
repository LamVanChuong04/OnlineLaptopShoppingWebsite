package com.fia.project_ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fia.project_ecommerce.model.Order;
import com.fia.project_ecommerce.model.User;

public interface OrderRepository  extends JpaRepository<Order, Long>{
    List<Order> findByUser(User user);
}
