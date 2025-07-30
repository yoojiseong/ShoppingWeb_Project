package com.busanit501.shoppingweb_project.repository.search;

import com.busanit501.shoppingweb_project.domain.QReview;
import com.busanit501.shoppingweb_project.domain.Review;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ReviewRepositoryCustom의 구현체.
 * Querydsl을 사용하여 복잡한 리뷰 페이징 쿼리를 처리합니다.
 */
@Repository
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 특정 상품 ID에 해당하는 리뷰 목록을 페이징하여 반환합니다.
     * 
     * @param productId      상품 ID
     * @param pageRequestDTO 페이징 요청 정보
     * @return 페이징된 리뷰 목록
     */
    @Override
    public Page<Review> getReviewsByProductId(Long productId, PageRequestDTO pageRequestDTO) {
        // 1. 페이징 정보 설정 (리뷰 ID를 기준으로 내림차순 정렬)
        Pageable pageable = pageRequestDTO.getPageable("reviewId");

        // 2. Querydsl 쿼리 준비
        QReview review = QReview.review;
        JPAQuery<Review> query = queryFactory.selectFrom(review);

        // 3. 조건 설정: 특정 상품 ID에 해당하는 리뷰만 필터링
        query.where(review.product.productId.eq(productId));

        // 4. 페이징 적용
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        // 5. 쿼리 실행 (결과 목록 가져오기)
        List<Review> content = query.fetch();

        // 6. 전체 카운트 쿼리 실행
        JPAQuery<Long> countQuery = queryFactory.select(review.count())
                .from(review)
                .where(review.product.productId.eq(productId));

        long total = countQuery.fetchOne();

        // 7. Page 객체로 변환하여 반환
        return new PageImpl<>(content, pageable, total);
    }
}