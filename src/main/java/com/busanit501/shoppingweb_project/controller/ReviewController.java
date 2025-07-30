package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import com.busanit501.shoppingweb_project.dto.PageResponseDTO;
import com.busanit501.shoppingweb_project.dto.ReviewDTO;
import com.busanit501.shoppingweb_project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * [lsr/feature/paging] 리뷰 페이징 목록 조회 API
     * 특정 상품에 대한 리뷰 목록을 페이징 처리하여 반환합니다.
     * 
     * @param productId      상품 ID
     * @param pageRequestDTO 페이징 요청 정보
     * @return 페이징된 리뷰 DTO 목록
     */
    @GetMapping("/{productId}")
    public ResponseEntity<PageResponseDTO<ReviewDTO>> getReviewList(
            @PathVariable Long productId,
            PageRequestDTO pageRequestDTO) {
        log.info("Fetching reviews for productId: {}, pageRequest: {}", productId, pageRequestDTO);
        PageResponseDTO<ReviewDTO> responseDTO = reviewService.getReviewList(productId, pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // ==================================================
    // 기존 주석 코드 보존 (다른 팀원 작업 내용 - CRUD)
    // ==================================================
    // @PostMapping
    // public ResponseEntity<ReviewResponseDto> createReview(
    // @PathVariable Long productId,
    // @RequestBody ReviewRequestDto requestDto) {
    // ReviewResponseDto responseDto = reviewService.createReview(productId,
    // requestDto);
    // return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    // }
    //
    // @GetMapping
    // public ResponseEntity<List<ReviewResponseDto>>
    // getAllReviewsByProductId(@PathVariable Long productId) {
    // List<ReviewResponseDto> reviews =
    // reviewService.getAllReviewsByProductId(productId);
    // return ResponseEntity.ok(reviews);
    // }
    //
    // @GetMapping("/{reviewId}")
    // public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long
    // reviewId) {
    // ReviewResponseDto review = reviewService.getReview(reviewId);
    // return ResponseEntity.ok(review);
    // }
    //
    //
    // @PutMapping("/{reviewId}")
    // public ResponseEntity<ReviewResponseDto> updateReview(
    // @PathVariable Long reviewId,
    // @RequestBody ReviewRequestDto requestDto) {
    // ReviewResponseDto updatedReview = reviewService.updateReview(reviewId,
    // requestDto);
    // return ResponseEntity.ok(updatedReview);
    // }
    //
    // @DeleteMapping("/{reviewId}")
    // public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
    // reviewService.deleteReview(reviewId);
    // return ResponseEntity.noContent().build();
    // }
}