package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.ProductDetailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailImageRepository extends JpaRepository<ProductDetailImage, Long> {

    // 특정 상품 ID에 해당하는 모든 상세 이미지를 순서(ord)에 따라 조회
    List<ProductDetailImage> findByProductIdOrderByOrdAsc(Long productId);
}