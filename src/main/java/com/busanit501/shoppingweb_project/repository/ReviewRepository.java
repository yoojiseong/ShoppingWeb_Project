package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Review;
import com.busanit501.shoppingweb_project.repository.search.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
    @Query("select b from Product b where b.productId = :productId")
    List<Review> findByProductId(Long productId);
}