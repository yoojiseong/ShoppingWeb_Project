package com.busanit501.shoppingweb_project.domain; // ✅ 패키지 경로 확인

import com.busanit501.shoppingweb_project.domain.enums.ProductCategory; // ✅ 임포트 경로 확인
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // 상품 고유 식별자 (PK)

    private String productName; // 상품 이름

    private BigDecimal price; // 💾 가격 (BigDecimal)

    private int stock; // 💾 재고 수량

    @Enumerated(EnumType.STRING) // Enum 타입을 DB에 문자열로 저장하도록 설정 (중요!)
    private ProductCategory productTag; // 💾 카테고리 (Enum)

    public void removeStock(int quantity) {
        int restStock = this.stock - quantity;
        if (restStock < 0) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + this.stock);
        }
        this.stock = restStock;
    }

    public void addStock(int quantity) {
        this.stock += quantity;
    }

    // ✅ Review 연관 관계 설정 시작
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // 편의 메서드: Product에 Review를 추가
    public void addReview(Review review) {
        this.reviews.add(review);
        review.setProduct(this);
    }

    // 편의 메서드: Product에서 Review를 제거
    public void removeReview(Review review) {
        this.reviews.remove(review);
        review.setProduct(null);
    }
    // ✅ Review 연관 관계 설정 끝

}