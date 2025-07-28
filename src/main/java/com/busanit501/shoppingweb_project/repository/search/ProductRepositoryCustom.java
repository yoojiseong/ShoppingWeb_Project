package com.busanit501.shoppingweb_project.repository.search;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    /**
     * 상품 목록을 동적 쿼리와 페이징을 적용하여 조회합니다.
     * @param pageable 페이징 정보
     * @return 페이징된 상품 목록
     */
    Page<Product> search(Pageable pageable);

}