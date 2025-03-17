package com.fia.project_ecommerce.controller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fia.project_ecommerce.model.Product;
import com.fia.project_ecommerce.model.User;
import com.fia.project_ecommerce.model.cart;
import com.fia.project_ecommerce.model.cartDetail;
import com.fia.project_ecommerce.repository.CartRepository;
import com.fia.project_ecommerce.repository.ProductRepository;
import com.fia.project_ecommerce.repository.CartDetailRepository;
import com.fia.project_ecommerce.service.UserService;
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    public ProductService(ProductRepository productRepository,
                        CartRepository cartRepository,
                        CartDetailRepository cartDetailRepository,
                        UserService userService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
    }
    // tạo product
    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }
    public List<Product> fetchProducts() {
        return this.productRepository.findAll();
    }
    
    public  void deleteProduct(long id){
        this.productRepository.deleteById(id);
    }
    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }
    public void handleAddProductToCart(String email, long productId) {
 
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            // check user đã có Cart chưa ? nếu chưa -> tạo mới
            cart cart = this.cartRepository.findByUser(user);

            if (cart == null) {
                // tạo mới cart
                cart otherCart = new cart();
                otherCart.setUser(user);
                otherCart.setSum(1);

                cart = this.cartRepository.save(otherCart);
            }

            // save cart_detail
            // tìm product by id

            Optional<Product> productOptional = this.productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product realProduct = productOptional.get();

                cartDetail cd = new cartDetail();
                cd.setCart(cart);
                cd.setProduct(realProduct);
                cd.setPrice(realProduct.getPrice());
                cd.setQuantity(1);
                this.cartDetailRepository.save(cd);
            }

        }
    }
    

}
