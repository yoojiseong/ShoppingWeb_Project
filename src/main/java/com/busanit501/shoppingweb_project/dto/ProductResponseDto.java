package com.busanit501.shoppingweb_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int stock;
    // productTag 필드와 생성자는 다음 단계에서 추가
}