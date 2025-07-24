package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.ProductRequestDto;
import com.busanit501.shoppingweb_project.dto.ProductResponseDto;
import com.busanit501.shoppingweb_project.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



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
}