package com.fia.project_ecommerce.controller.client;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fia.project_ecommerce.controller.service.ProductService;
import com.fia.project_ecommerce.model.Product;
import com.fia.project_ecommerce.model.User;
import com.fia.project_ecommerce.model.dto.RegisterDTO;
import com.fia.project_ecommerce.service.UserService;


import org.springframework.web.bind.annotation.PostMapping;



import jakarta.validation.Valid;



@Controller
public class HomePageController {
    private final ProductService productService;
    private final PasswordEncoder passwordEncoder; // Needed for password encryption
    private final UserService userService;
    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/")
    public String getProduct(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);

        return "client/homepage/homepage";
    }
    @GetMapping("/register")
     public String getRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDTO());
         return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(
             @ModelAttribute("register") @Valid RegisterDTO registerDTO,
             BindingResult bindingResult) {
 
         // validate
        if (bindingResult.hasErrors()) {
             return "client/auth/register";
         }
        User user = this.userService.dtoToUser(registerDTO);
        String password = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRole(this.userService.getRoleByName("USER"));
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String getLoginPage(Model model) {
         return "client/auth/login";
     }
     @GetMapping("/access-deny")
     public String getDenyPage(Model model) {
         return "client/auth/deny";
     }
 
}
