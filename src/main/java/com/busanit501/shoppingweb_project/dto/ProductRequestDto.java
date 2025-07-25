package com.busanit501.shoppingweb_project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {

    private String productName;
    private BigDecimal price;
    private int stock;
    private String productTag;
}