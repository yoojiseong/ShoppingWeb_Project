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
    private Long productId;

    private String productName;

    private BigDecimal price;

    private int stock;

    @Enumerated(EnumType.STRING)
    private ProductCategory productTag;

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


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    public void addReview(Review review) {
        this.reviews.add(review);
        review.setProduct(this);
    }


    public void removeReview(Review review) {
        this.reviews.remove(review);
        review.setProduct(null);
    }


}