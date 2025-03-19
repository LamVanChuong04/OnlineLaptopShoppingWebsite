package com.fia.project_ecommerce.service;

import java.util.List;


import org.springframework.stereotype.Service;
import com.fia.project_ecommerce.controller.admin.OrderController;
import com.fia.project_ecommerce.controller.admin.ProductController;
import com.fia.project_ecommerce.model.Role;
import com.fia.project_ecommerce.model.User;
import com.fia.project_ecommerce.model.dto.RegisterDTO;
import com.fia.project_ecommerce.repository.OrderRepository;
import com.fia.project_ecommerce.repository.ProductRepository;
import com.fia.project_ecommerce.repository.RoleRepository;
import com.fia.project_ecommerce.repository.UserRepository;

@Service
public class UserService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    // constructor
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
    ProductRepository productRepository,
             OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository; 
        this.orderRepository = orderRepository; 
    }
    // lấy tất cả user
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
    // lấy user theo email
    public List<User> getAllUsersByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    // hàm save user sau khi tạo/delete
    public User handleSaveUser(User user) {
        User users = this.userRepository.save(user);
        return users;
    }
    // lấy user theo id
    public User getUserById(long id) {
        return this.userRepository.findById(id);// trả về user ứng với id
    }
    // xóa user theo id
    public void deleteAUser(long id) {
        this.userRepository.deleteById(id);
    }
    // lấy role theo id
    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }
    // DTO
    public User dtoToUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setAddress(registerDTO.getAddress());
        user.setPassword(registerDTO.getPassword());
        return user;
    }
    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
    public long countUsers() {
        return this.userRepository.count();
    }

    public long countProducts() {
        return this.productRepository.count();
    }

    public long countOrders() {
        return this.orderRepository.count();
    }
}

