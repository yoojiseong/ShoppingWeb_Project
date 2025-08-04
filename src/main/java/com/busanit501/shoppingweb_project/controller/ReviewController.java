package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import com.busanit501.shoppingweb_project.dto.PageResponseDTO;
import com.busanit501.shoppingweb_project.dto.ReviewDTO;
//import com.busanit501.shoppingweb_project.security.CustomUserDetails;
import com.busanit501.shoppingweb_project.security.MemberSecurityDTO;
import com.busanit501.shoppingweb_project.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<Void> registerReview(
            @RequestBody ReviewDTO dto,
            @AuthenticationPrincipal MemberSecurityDTO userDetails
    ) {

        if (userDetails == null) {
//            log.warn("리뷰 등록 요청: 로그인 정보 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("ReveiwController 리뷰 등록 진행 : " + userDetails.getUsername()+ dto.getReviewContent());
        reviewService.createReview(dto, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    // 리뷰 목록 조회 (페이징 포함)
    @GetMapping
    public ResponseEntity<PageResponseDTO<ReviewDTO>> getReviews(PageRequestDTO requestDTO) {
        log.info("ReviewController에서 작업중 RequestDTO확인 : "+ requestDTO.toString());
        PageResponseDTO<ReviewDTO> response = reviewService.getList(requestDTO);
        log.info("ReviewController에서 반환할 데이터 : "+ response.toString());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long reviewId,
                                             @RequestBody Map<String, String> body,
                                             @AuthenticationPrincipal MemberSecurityDTO userDetails) throws AccessDeniedException {
        if (userDetails == null) {
//            log.warn("리뷰 등록 요청: 로그인 정보 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String updatedContent = body.get("reviewContent");
        log.info("ReviewController에서 userDetails에 뭐 있는지 확인중 : "+ userDetails);
        reviewService.updateReview(reviewId, updatedContent, userDetails.getId());
        return ResponseEntity.ok().build();
    }
//    @PostMapping
//    public ResponseEntity<ReviewResponseDto> createReview(
//            @PathVariable Long productId,
//            @RequestBody ReviewRequestDto requestDto) {
//        ReviewResponseDto responseDto = reviewService.createReview(productId, requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ReviewResponseDto>> getAllReviewsByProductId(@PathVariable Long productId) {
//        List<ReviewResponseDto> reviews = reviewService.getAllReviewsByProductId(productId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    @GetMapping("/{reviewId}")
//    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId) {
//        ReviewResponseDto review = reviewService.getReview(reviewId);
//        return ResponseEntity.ok(review);
//    }
//
//
//    @PutMapping("/{reviewId}")
//    public ResponseEntity<ReviewResponseDto> updateReview(
//            @PathVariable Long reviewId,
//            @RequestBody ReviewRequestDto requestDto) {
//        ReviewResponseDto updatedReview = reviewService.updateReview(reviewId, requestDto);
//        return ResponseEntity.ok(updatedReview);
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
//        reviewService.deleteReview(reviewId);
//        return ResponseEntity.noContent().build();
//    }

}