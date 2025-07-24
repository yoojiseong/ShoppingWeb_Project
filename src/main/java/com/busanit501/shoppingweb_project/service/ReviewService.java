package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.domain.Review;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import com.busanit501.shoppingweb_project.repository.ReviewRepository;
import com.busanit501.shoppingweb_project.dto.ReviewRequestDto;
import com.busanit501.shoppingweb_project.dto.ReviewResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewResponseDto createReview(Long productId, ReviewRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. 상품 ID: " + productId));

        Review review = new Review();
        review.setReviewContent(requestDto.getReviewContent());
        review.setRating(requestDto.getRating());
        review.setProduct(product);

        product.addReview(review);

        Review savedReview = reviewRepository.save(review);

        return new ReviewResponseDto(savedReview);
    }

    // 다른 메서드들은 다음 단계에서 추가
}