package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String reviewContent;
    private int rating;
    private LocalDateTime createdAt;
    private Long productId; // productId 필드 추가

    // Review 엔티티를 받아 DTO로 변환하는 생성자 추가
    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.reviewContent = review.getReviewContent();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        // review.getProduct()가 null이 아닐 경우 productId를 가져오고, 아니면 null
        this.productId = (review.getProduct() != null) ? review.getProduct().getProductId() : null;
    }
}