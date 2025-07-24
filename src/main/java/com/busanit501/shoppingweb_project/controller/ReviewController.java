package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.ReviewRequestDto;
import com.busanit501.shoppingweb_project.dto.ReviewResponseDto;
import com.busanit501.shoppingweb_project.service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable Long productId,
            @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto responseDto = reviewService.createReview(productId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}