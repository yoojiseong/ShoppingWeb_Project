package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import com.busanit501.shoppingweb_project.dto.ProductRequestDto;
import com.busanit501.shoppingweb_project.dto.ProductResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = new Product();
        product.setProductName(requestDto.getProductName());
        product.setPrice(requestDto.getPrice());
        product.setStock(requestDto.getStock());
        product.setProductTag(ProductCategory.fromKoreanName(requestDto.getProductTag()));

        Product savedProduct = productRepository.save(product);
        return new ProductResponseDto(savedProduct);
    }

    // 다른 메서드들은 다음 단계에서 추가
}