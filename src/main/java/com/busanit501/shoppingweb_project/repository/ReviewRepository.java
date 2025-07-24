package com.busanit501.shoppingweb_project.repository; // ✅ 패키지 경로 변경

import com.busanit501.shoppingweb_project.domain.Review; // ✅ 임포트 경로 변경
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_ProductId(Long productId);
}