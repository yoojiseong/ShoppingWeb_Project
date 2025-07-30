package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Review;
import com.busanit501.shoppingweb_project.dto.PageRequestDTO;
import com.busanit501.shoppingweb_project.dto.PageResponseDTO;
import com.busanit501.shoppingweb_project.dto.ReviewDTO;
import com.busanit501.shoppingweb_project.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    /**
     * [lsr/feature/paging] 리뷰 페이징 목록 기능 구현
     * Repository를 호출하여 특정 상품의 리뷰 목록을 가져오고, DTO로 변환하여 반환합니다.
     *
     * @param productId      상품 ID
     * @param pageRequestDTO 페이징 요청 정보
     * @return 페이징된 리뷰 DTO 목록
     */
    @Override
    public PageResponseDTO<ReviewDTO> getReviewList(Long productId, PageRequestDTO pageRequestDTO) {
        // 1. Repository 호출하여 페이징된 Entity 목록을 가져옴
        Page<Review> result = reviewRepository.getReviewsByProductId(productId, pageRequestDTO);

        // 2. Entity List -> DTO List 변환 (날짜 포맷팅 포함)
        List<ReviewDTO> dtoList = result.getContent().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        // 3. PageResponseDTO 생성 및 반환
        return PageResponseDTO.<ReviewDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .totalCount(result.getTotalElements())
                .build();
    }

    /**
     * [lsr/feature/paging] Review Entity를 ReviewDTO로 변환하는 메소드
     * 이유: createdAt 필드의 타입이 달라(LocalDateTime -> String) ModelMapper 자동 변환이 불가하므로,
     * 수동으로 날짜 포맷팅을 적용하기 위해 별도의 메소드로 분리했습니다.
     * 
     * @param review 변환할 Review Entity
     * @return 변환된 ReviewDTO
     */
    private ReviewDTO entityToDto(Review review) {
        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        reviewDTO.setCreatedAt(review.getCreatedAt().format(formatter));
        return reviewDTO;
    }
}