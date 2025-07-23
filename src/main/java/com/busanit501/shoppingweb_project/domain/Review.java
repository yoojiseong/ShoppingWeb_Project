package com.busanit501.shoppingweb_project.domain; // ✅ 패키지 경로 확인

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String reviewContent;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ✅ Product 엔티티와 연관 관계 설정 시작
    // ManyToOne: 여러 Review가 하나의 Product에 연결될 수 있어.
    // fetch = FetchType.LAZY: Product 정보를 즉시 가져오지 않고, 실제로 필요할 때 가져오도록 설정. 성능상 이점!
    // @JoinColumn: 외래 키(FK) 매핑. review 테이블에 'product_id' 컬럼이 생성돼.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false) // 'product_id'라는 이름으로 외래 키 컬럼 생성, null 허용 안 함
    private Product product; // 이 리뷰가 달린 상품 (외래 키 역할)
    // ✅ Product 엔티티와 연관 관계 설정 끝

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}