package com.busanit501.shoppingweb_project.repository.search;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {
    /**
     * [lsr/feature/paging] 상품 목록/검색 페이징 기능
     * Querydsl을 사용하여 페이징 및 동적 검색 쿼리를 처리합니다.
     * 
     * @param pageRequestDTO 페이징 및 검색 조건
     * @return 페이징된 상품 목록
     */
    Page<Product> search(PageRequestDTO pageRequestDTO);
}