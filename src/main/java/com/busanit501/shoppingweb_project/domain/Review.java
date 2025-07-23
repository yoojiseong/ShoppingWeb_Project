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

    // Product와의 연관 관계는 다음 단계에서 추가

    // ✅ 생성 시간 자동화 메서드 추가 시작
    // 엔티티가 영속성 컨텍스트에 저장되기 전에 실행되는 콜백 메서드.
    // 즉, DB에 INSERT 되기 직전에 createdAt 필드에 현재 시간을 자동으로 설정.
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    // ✅ 생성 시간 자동화 메서드 추가 끝
}