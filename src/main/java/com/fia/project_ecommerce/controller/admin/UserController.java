package com.fia.project_ecommerce.controller.admin;

import com.fia.project_ecommerce.controller.service.UploadFileService;
import com.fia.project_ecommerce.model.User;

import com.fia.project_ecommerce.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.GetMapping;

//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;





// @RestController
// public class UserController {
//     private UserService userService;
    
//     public UserController(UserService userService){
//         this.userService = userService;
//     }
//     @GetMapping("/")
//     public String  getHomePage(){
//         return this.userService.getHomePage();
//     }
// }

@Controller
public class UserController {
    private final UserService userService;
    private final UploadFileService uploadService;
    private final PasswordEncoder passwordEncoder;


    public UserController(ServletContext servletContext, UserService userService, UploadFileService uploadService, PasswordEncoder passwordEncoder) {
        this.uploadService = uploadService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
     // create user
    @GetMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }
    @PostMapping(value = "/admin/user/create")
     public String createUserPage(Model model,
            @ModelAttribute("newUser") @Valid User chuong,
            BindingResult newUserBindingResult,
            @RequestParam("uploadFile") MultipartFile file) {
        
        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>" + error.getField() + " - " + error.getDefaultMessage());
         }

         if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(chuong.getPassword());

        chuong.setAvatar(avatar);
        chuong.setPassword(hashPassword);
        chuong.setRole(this.userService.getRoleByName(chuong.getRole().getName()));
        this.userService.handleSaveUser(chuong);
        return "redirect:/admin/user";
     }
    // lấy danh sách user
    @GetMapping("/admin/user")
    public String getUserPage(Model model,
             @RequestParam("page") Optional<String> pageOptional) {
         int page = 1;
         try {
             if (pageOptional.isPresent()) {
                 // convert from String to int
                 page = Integer.parseInt(pageOptional.get());
             } else {
                 // page = 1
             }
         } catch (Exception e) {
             // page = 1
             // TODO: handle exception
         }
 
         Pageable pageable = PageRequest.of(page - 1, 6);
         Page<User> usersPage = this.userService.getAllUsers(pageable);
         List<User> users = usersPage.getContent();
         model.addAttribute("users", users);
 
         model.addAttribute("currentPage", page);
         model.addAttribute("totalPages", usersPage.getTotalPages());
         return "admin/user/show";
     }
    // lấy thông tin chi tiết user
    @GetMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);// hứng user được trả về từ service
        model.addAttribute("user", user);// truyền dữ liệu từ controller sang view

        return "admin/user/detail";
    }

    // update user
    @GetMapping("/admin/user/update/{id}") // GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User user) {
        User currentUser = this.userService.getUserById(user.getId());
        if (currentUser != null) {
            currentUser.setAddress(user.getAddress());
            currentUser.setFullName(user.getFullName());
            currentUser.setPhone(user.getPhone());

            // bug here
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }
    // delete
    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUser(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }
    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User u) {
        this.userService.deleteAUser(u.getId());
        return "redirect:/admin/user";
    }

}
