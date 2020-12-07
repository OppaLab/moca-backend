package com.moca.springboot.controller;

import com.moca.springboot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping("/admin")
    public String adminPage(Model model) {
        adminService.adminPage(model);
        return "index";
    }

}
