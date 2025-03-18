package com.fia.project_ecommerce.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fia.project_ecommerce.controller.service.ProductService;
import com.fia.project_ecommerce.model.Product;
import com.fia.project_ecommerce.model.User;
import com.fia.project_ecommerce.model.cart;
import com.fia.project_ecommerce.model.cartDetail;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;





@Controller
public class ItemController {
    private final ProductService productService;
    
    public ItemController(ProductService productService) {
        this.productService = productService;
    }

    //  xem chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String getMethodName(Model model, @PathVariable long id) {
        Product pr = this.productService.fetchProductById(id).get();// hứng user được trả về từ service
        model.addAttribute("product", pr);// truyền dữ liệu từ controller sang view
        model.addAttribute("id", id);
        return "client/product/detail";
    }
    // thêm sản phẩm vào giỏ hàng
     @PostMapping("/add-product-to-cart/{id}")
     public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
         HttpSession session = request.getSession(false);
 
         long productId = id;
         String email = (String) session.getAttribute("email");
 
         this.productService.handleAddProductToCart(email, productId, session);
 
         return "redirect:/";
     }
     // get cart
     @GetMapping("/cart")
     public String getCartPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        cart cart = this.productService.fetchByUser(currentUser);

        List<cartDetail> cartDetails = cart.getCartDetail();

        double totalPrice = 0;
        for (cartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "client/cart/show";
    }
}
