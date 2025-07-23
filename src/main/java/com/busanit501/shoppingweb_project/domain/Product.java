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

    // 나머지 필드와 메서드는 다음 단계에서 추가

}