package com.fia.project_ecommerce.controller.service;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fia.project_ecommerce.controller.service.specification.ProductSpec;
import com.fia.project_ecommerce.model.Order;
import com.fia.project_ecommerce.model.OrderDetail;
import com.fia.project_ecommerce.model.Product;

import com.fia.project_ecommerce.model.User;
import com.fia.project_ecommerce.model.cart;
import com.fia.project_ecommerce.model.cartDetail;
import com.fia.project_ecommerce.model.dto.ProductCriteriaDTO;
import com.fia.project_ecommerce.repository.CartRepository;
import com.fia.project_ecommerce.repository.OrderDetailRepository;
import com.fia.project_ecommerce.repository.OrderRepository;
import com.fia.project_ecommerce.repository.ProductRepository;
import com.fia.project_ecommerce.repository.CartDetailRepository;
import com.fia.project_ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    public ProductService(ProductRepository productRepository,
                        CartRepository cartRepository,
                        CartDetailRepository cartDetailRepository,
                        UserService userService, 
                        OrderRepository orderRepository, OrderDetailRepository orderDetailRepository ) {
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;

    }
    // tạo product
    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }
    
     public Page<Product> fetchProducts(Pageable page) {
         return this.productRepository.findAll(page);
     }
    
    public void deleteProduct(long id){
        this.productRepository.deleteById(id);
    }
    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }
    public cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }
    public void handleAddProductToCart(String email, long productId, HttpSession session, long quantity) {
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
                    cd.setQuantity(quantity);
                    this.cartDetailRepository.save(cd);
                    // update số lượng sản phẩm trong giỏ hàng
                    int s = cart.getSum() + 1;
                    cart.setSum(s);
                    this.cartRepository.save(cart);
                    session.setAttribute("sum", s);                   
                }else{
                    oldDetail.setQuantity(oldDetail.getQuantity() + quantity);
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
    // function đặt hàng
    public void handlerPlaceOrder(User user, HttpSession session, String recieverName, String recieverAddress, String receiverPhone){

        // step 1: create orderDetail
        cart cart = this.cartRepository.findByUser(user);
        if(cart != null){
            List<cartDetail> cartDetails = cart.getCartDetail();

            if(cartDetails != null){
                        // create order
                Order order = new Order();
                order.setUser(user);
                order.setReceiverName(recieverName);
                order.setReceiverAddress(recieverAddress);
                order.setReceiverPhone(receiverPhone);
                this.orderRepository.save(order);

                order.setStatus("PENDING");
 
                 double sum = 0;
                 for (cartDetail cd : cartDetails) {
                     sum += cd.getPrice();
                 }
                 order.setTotalPrice(sum);
                 order = this.orderRepository.save(order);
                for(cartDetail cd: cartDetails){
                    OrderDetail od = new OrderDetail();
                    od.setOrder(order);
                    od.setProduct(cd.getProduct());
                    od.setQuantity(cd.getQuantity());
                    od.setPrice(cd.getPrice());


                    this.orderDetailRepository.save(od);
                }
            }
            // step 2: delete cartDetail and cart
            for(cartDetail cd : cartDetails){
                this.cartDetailRepository.deleteById(cd.getId());
            }
            this.cartRepository.deleteById(cart.getId());
            // step 3: update session
            session.setAttribute("sum", 0 );
        }
    }
    public Page<Product> fetchProductsWithSpec(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        if (productCriteriaDTO.getTarget() == null
                && productCriteriaDTO.getFactory() == null
                && productCriteriaDTO.getPrice() == null) {
            return this.productRepository.findAll(page);
        }

        Specification<Product> combinedSpec = Specification.where(null);

        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpec.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpec.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        if (productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()) {
            Specification<Product> currentSpecs = this.buildPriceSpecification(productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        return this.productRepository.findAll(combinedSpec, page);
    }

    // case 6
    public Specification<Product> buildPriceSpecification(List<String> price) {
        Specification<Product> combinedSpec = Specification.where(null); // disconjunction
        for (String p : price) {
            double min = 0;
            double max = 0;

            // Set the appropriate min and max based on the price range string
            switch (p) {
                case "duoi-10-trieu":
                    min = 1;
                    max = 10000000;
                    break;
                case "10-15-trieu":
                    min = 10000000;
                    max = 15000000;
                    break;
                case "15-20-trieu":
                    min = 15000000;
                    max = 20000000;
                    break;
                case "tren-20-trieu":
                    min = 20000000;
                    max = 200000000;
                    break;
            }

            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpec.matchMultiplePrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec);
            }
        }

        return combinedSpec;
    }


}
