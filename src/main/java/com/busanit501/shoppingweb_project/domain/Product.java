package com.busanit501.shoppingweb_project.domain; // ✅ 패키지 경로 확인

import com.busanit501.shoppingweb_project.domain.enums.ProductCategory; // ✅ Enum 임포트 추가
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    // ✅ ProductCategory 필드 추가 시작
    @Enumerated(EnumType.STRING) // Enum 타입을 DB에 문자열로 저장하도록 설정 (중요!)
    private ProductCategory productTag; // 💾 카테고리 (Enum)
    // ✅ ProductCategory 필드 추가 끝

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

    // 나머지 필드와 메서드는 다음 단계에서 추가

}