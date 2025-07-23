package com.busanit501.shoppingweb_project.domain; // ✅ 패키지 경로 확인

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal; // BigDecimal 임포트

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

    // ✅ 재고 관리 메서드 추가 시작
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
    // ✅ 재고 관리 메서드 추가 끝

    // 나머지 필드와 메서드는 다음 단계에서 추가

}