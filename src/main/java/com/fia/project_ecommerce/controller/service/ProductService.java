package com.fia.project_ecommerce.controller.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;

import com.fia.project_ecommerce.model.Product;
import com.fia.project_ecommerce.model.User;
import com.fia.project_ecommerce.model.cart;
import com.fia.project_ecommerce.model.cartDetail;
import com.fia.project_ecommerce.repository.CartRepository;
import com.fia.project_ecommerce.repository.ProductRepository;
import com.fia.project_ecommerce.repository.CartDetailRepository;
import com.fia.project_ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {

    private final DaoAuthenticationProvider authProvider;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    public ProductService(ProductRepository productRepository,
                        CartRepository cartRepository,
                        CartDetailRepository cartDetailRepository,
                        UserService userService, DaoAuthenticationProvider authProvider) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.authProvider = authProvider;
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
    public cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }
    public void handleAddProductToCart(String email, long productId, HttpSession session) {
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            // check user đã có Cart chưa ? nếu chưa -> tạo mới
            cart cart = this.cartRepository.findByUser(user);

            if (cart == null) {
                // tạo mới cart
                cart otherCart = new cart();
                otherCart.setUser(user);
                otherCart.setSum(0);

                cart = this.cartRepository.save(otherCart);
            }
            // save cart_detail
            // tìm product by id
            Optional<Product> productOptional = this.productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product realProduct = productOptional.get();
                cartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);
                if(oldDetail == null){
                    cartDetail cd = new cartDetail();
                    cd.setCart(cart);
                    cd.setProduct(realProduct);
                    cd.setPrice(realProduct.getPrice());
                    cd.setQuantity(1);
                    this.cartDetailRepository.save(cd);
                    // update số lượng sản phẩm trong giỏ hàng
                    int s = cart.getSum() + 1;
                    cart.setSum(s);
                    session.setAttribute("sum", s);
                    
                }else{
                    oldDetail.setQuantity(oldDetail.getQuantity() + 1);
                    this.cartDetailRepository.save(oldDetail);
                }
                
            }

        }
    }
    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
        Optional<cartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetailOptional.isPresent()) {
            cartDetail cartDetail = cartDetailOptional.get();

            cart currentCart = cartDetail.getCart();
            // delete cart-detail
            this.cartDetailRepository.deleteById(cartDetailId);

            // update cart
            if (currentCart.getSum() > 1) {
                // update current cart
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);
                session.setAttribute("sum", s);
                this.cartRepository.save(currentCart);
            } else {
                // delete cart (sum = 1)
                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
            }
        }
    }
    public void handleUpdateCartBeforeCheckout(List<cartDetail> cartDetails) {
        for (cartDetail cartDetail : cartDetails) {
            Optional<cartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
            if (cdOptional.isPresent()) {
                cartDetail currentCartDetail = cdOptional.get();
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }

}
