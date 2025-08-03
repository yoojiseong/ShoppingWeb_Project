package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import com.busanit501.shoppingweb_project.dto.PageResponseDTO;
import com.busanit501.shoppingweb_project.dto.ReviewDTO;

import java.nio.file.AccessDeniedException;

public interface ReviewService {
    void createReview(ReviewDTO dto, Long memberId);
    PageResponseDTO<ReviewDTO> getList(PageRequestDTO requestDTO);
    void updateReview(Long reviewId, String newContent, Long memberId) throws AccessDeniedException;
}
