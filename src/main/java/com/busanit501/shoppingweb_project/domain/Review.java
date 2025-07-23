package com.busanit501.shoppingweb_project.domain; // ✅ 패키지 경로 확인

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime; // LocalDateTime 임포트

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reviews") // DB 테이블 이름을 'reviews'로 명시
public class Review {

    @Id // 기본 키(Primary Key)임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 (DB에 위임)
    private Long reviewId; // 리뷰 고유 식별자 (PK)

    @Column(nullable = false) // 컬럼 설정: null 값 허용 안 함
    private String reviewContent; // 리뷰 내용

    @Column(nullable = false) // 컬럼 설정: null 값 허용 안 함
    private int rating; // 별점 (1~5)

    @Column(nullable = false, updatable = false) // 컬럼 설정: null 값 허용 안 함, 업데이트 불가능
    private LocalDateTime createdAt; // 리뷰 생성 시간

    // Product와의 연관 관계는 다음 단계에서 추가
    // @ManyToOne
    // private Product product;

    // @PrePersist 메서드는 다음 단계에서 추가
    // public void prePersist() { ... }
}