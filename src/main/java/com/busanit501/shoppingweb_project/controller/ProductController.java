package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.ProductRequestDto;
import com.busanit501.shoppingweb_project.dto.ProductResponseDto;
import com.busanit501.shoppingweb_project.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.createProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        ProductResponseDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

}