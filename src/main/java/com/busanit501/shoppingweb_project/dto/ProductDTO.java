package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;

    @NotEmpty
    private String productName;
    @NotNull
    private BigDecimal price;
    private int stock;
    private ProductCategory productTag;
    private String imageFileName; // 이미지 파일 이름 담을 필드 추가

    private List<String> fileNames;

    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .productTag(ProductCategory.valueOf(product.getProductTag().name())) // Enum을 문자열로 변환
                .build();
    }

}
