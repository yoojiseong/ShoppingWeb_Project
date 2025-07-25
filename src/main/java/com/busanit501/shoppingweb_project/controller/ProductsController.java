package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // ← RestController 말고 Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;

    @GetMapping("/home")
    public String home() {
        return "home"; // src/main/resources/templates/home.html
    }
}
