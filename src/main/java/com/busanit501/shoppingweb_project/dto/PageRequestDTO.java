package com.busanit501.shoppingweb_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 페이징 요청 정보를 담는 DTO (Data Transfer Object)
 * 역할: 클라이언트(브라우저)에서 서버로 "몇 페이지의 몇 개 데이터를 보여주세요" 라는 요청을 보낼 때,
 * 그 요청 정보를 담는 규격화된 상자 역할을 합니다.
 * 특징: 상품, 댓글 등 여러 곳에서 페이징 처리가 필요하므로, 특정 도메인에 종속되지 않는
 * 범용 DTO로 설계하여 코드 재사용성을 높였습니다.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    // 현재 페이지 번호. 클라이언트가 보내는 값이며, 기본값은 1로 설정하여 안정성을 높임.
    @Builder.Default
    private int page = 1;

    // 한 페이지에 보여줄 데이터의 개수.
    @Builder.Default
    private int size = 10;

    /**
     * [핵심 로직] 이 DTO의 정보를 바탕으로 Spring Data JPA가 사용하는 Pageable 객체를 생성.
     * 역할: 순수한 요청 데이터(page, size)를 JPA가 이해할 수 있는 형식(Pageable)으로 변환하는 다리 역할.
     * 서비스 로직에서는 이 메소드를 호출하여 JPA에 페이징과 정렬을 요청하게 됩니다.
     * 
     * @param props 정렬 기준으로 사용할 엔티티의 속성 이름들 (예: "productId", "reviewId")
     * @return 정렬 조건이 포함된 Pageable 객체
     */
    public Pageable getPageable(String... props) {
        // Pageable 객체 생성: PageRequest.of(페이지번호, 사이즈, 정렬조건)
        // - 페이지번호: JPA는 0부터 시작하므로, 사용자가 요청한 페이지(1부터 시작)에서 -1을 해줘야 함.
        // - 정렬조건: Sort.by(props).descending() -> props로 들어온 속성을 기준으로 내림차순(최신순) 정렬.
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }
}