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
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;

    @NotEmpty(message = "상품 이름은 필수 항목입니다.")
    private String productName;

    private String productDescription;

    @NotNull(message = "가격은 필수 항목입니다.")
    private BigDecimal price;

    private int stock;

    private ProductCategory productTag;

    private List<String> imageUrls;
    private String thumbnailUrl;

    public static ProductDTO fromEntity(Product product) {
        if (product == null) {
            return null;
        }

        String thumbnailUrl = null;
        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            String originalFullUrl = product.getImageUrls().get(0);

            int lastSlashIndex = originalFullUrl.lastIndexOf("/");
            String originalFileName = (lastSlashIndex != -1) ? originalFullUrl.substring(lastSlashIndex + 1) : originalFullUrl;

            thumbnailUrl = "/displayImage/s_" + originalFileName;
        }

        return ProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .productTag(product.getProductTag())
                .imageUrls(product.getImageUrls() != null ? new ArrayList<>(product.getImageUrls()) : new ArrayList<>())
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}