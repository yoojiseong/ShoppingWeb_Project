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
    private double avgRate;
    private int rateCount;
    private String thumbnailFileName; // 이미지 파일 이름 담을 필드 추가

    private List<String> fileNames;

    public void setImageFileName(String fileName)
    {
        this.thumbnailFileName = fileName;
    }

    private boolean deletedThumbnail; // 기존 썸네일을 삭제할지 여부
    private List<String> deletedDetailImageNames; // 삭제할 상세 이미지 파일명 리스트


}
