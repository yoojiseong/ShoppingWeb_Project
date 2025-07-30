package com.busanit501.shoppingweb_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminPage() {
        // templates/admin.html 뷰 리턴 (Thymeleaf 사용 시)
        return "admin";
    }
}