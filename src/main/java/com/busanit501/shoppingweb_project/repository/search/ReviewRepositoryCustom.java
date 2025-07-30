package com.busanit501.shoppingweb_project.repository.search;

import com.busanit501.shoppingweb_project.domain.Review;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ReviewRepositoryCustom {
    /**
     * 특정 상품(productId)에 대한 리뷰 목록을 페이징 처리하여 반환합니다.
     * Querydsl을 사용하여 복잡한 쿼리를 처리하기 위해 별도의 Custom Repository로 분리했습니다.
     * 
     * @param productId      상품 ID
     * @param pageRequestDTO 페이징 요청 정보 (페이지 번호, 사이즈)
     * @return 페이징된 리뷰 목록
     */
    Page<Review> getReviewsByProductId(Long productId, PageRequestDTO pageRequestDTO);
}