package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    Optional<ProductImage> findByProduct_ProductIdAndThumbnail(Long productId, boolean thumbnail);
    Optional<ProductImage> findByProduct_ProductIdAndFileName(Long productId, String fileName);

    // 상품 ID로 모든 이미지 리스트 조회 메서드 추가
    List<ProductImage> findByProduct_ProductId(Long productId);
}