package com.busanit501.shoppingweb_project.repository.search;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.QProduct;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Product> search(Pageable pageable) {
        QProduct product = QProduct.product;
        
        // 1. select...from...
        JPAQuery<Product> query = queryFactory.selectFrom(product);

        // 2. where 조건 추가 (이 부분이 동적 쿼리의 핵심!)
        // BooleanBuilder는 if문을 사용해 동적으로 where 절의 조건을 조립해주는 역할을 합니다.
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        
        // 현재는 검색 조건이 없으므로 비워둡니다.
        // 향후 PageRequestDTO에서 검색어를 받아와서 여기에 조건을 추가할 예정입니다.
        // 예: if (pageRequestDTO.hasKeyword()) { ... }

        query.where(booleanBuilder);

        // 3. 페이징 처리
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        // 4. 쿼리 실행
        List<Product> content = query.fetch();
        long total = query.fetchCount(); // fetchCount()는 deprecated 되었지만, 간단한 예시로 사용.

        return new PageImpl<>(content, pageable, total);
    }
}