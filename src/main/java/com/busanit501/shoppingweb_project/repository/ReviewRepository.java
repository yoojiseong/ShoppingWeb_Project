package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.Review;
import com.busanit501.shoppingweb_project.repository.search.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    // [lsr/feature/rating] 특정 상품의 전체 리뷰 수를 계산
    long countByProduct(Product product);

    // [lsr/feature/rating] 특정 상품의 평균 평점을 계산 (리뷰가 없는 경우 null을 반환할 수 있으므로 Double로 받음)
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = :product")
    Double findAverageRatingByProduct(@Param("product") Product product);

    /*
     * ==================================================
     * [lsr/feature/paging] 기존 코드 보존 (주석 처리)
     * - 이유: 아래 메소드는 충돌 가능성이 있는 쿼리를 포함하고 있으며, 페이징 기능으로 대체되었습니다.
     * ==================================================
     */
    // @Query("select b from Product b where b.productId = :productId")
    // List<Review> findByProductId(Long productId);
}