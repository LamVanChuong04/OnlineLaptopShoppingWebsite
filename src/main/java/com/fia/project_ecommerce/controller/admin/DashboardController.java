package com.fia.project_ecommerce.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
 
    @GetMapping("/admin")
    public String getDashboard() {
        return "admin/dashboard/test";
     }
 }
