package com.busanit501.shoppingweb_project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 페이징 처리 결과를 담는 범용 DTO.
 * @param <E> 페이징될 데이터의 DTO 타입
 */
@Data
public class PageResponseDTO<E> {

    // 제네릭 타입으로 DTO 목록을 받음
    private List<E> dtoList;

    // 전체 데이터 개수
    private long totalCount;

    // 페이지 번호 목록
    private List<Integer> pageNumList;

    // 원본 페이지 요청 정보
    private PageRequestDTO pageRequestDTO;

    // 이전/다음 페이지 존재 여부
    private boolean prev, next;

    // 현재 페이지네이션의 시작/끝 번호
    private int start, end;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, long totalCount, PageRequestDTO pageRequestDTO) {
        this.dtoList = dtoList;
        this.totalCount = totalCount;
        this.pageRequestDTO = pageRequestDTO;

        // 페이지네이션 UI 계산
        // 1. UI 끝 번호 계산
        this.end = (int)(Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
        // 2. UI 시작 번호 계산
        this.start = this.end - 9;
        // 3. 데이터 기반 실제 마지막 페이지 계산
        int last = (int)(Math.ceil(totalCount / (double)pageRequestDTO.getSize()));
        // 4. UI 끝 번호 보정
        this.end = Math.min(end, last);

        // 5. 이전/다음 버튼 활성화 여부
        this.prev = this.start > 1;
        this.next = totalCount > (long)this.end * pageRequestDTO.getSize();

        // 6. 페이지 번호 목록 생성
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}