package com.busanit501.shoppingweb_project.dto;

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
    // productId 필드와 생성자는 다음 단계에서 추가
}