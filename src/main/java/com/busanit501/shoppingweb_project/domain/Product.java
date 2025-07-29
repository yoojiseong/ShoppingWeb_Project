package com.busanit501.shoppingweb_project.domain;

import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor // Lombok이 모든 필드를 인자로 받는 생성자를 자동 생성
@Builder // Lombok이 빌더 패턴을 자동 생성
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    private BigDecimal price;
    private int stock;

    @Enumerated(EnumType.STRING)
    private ProductCategory productTag;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_images",
            joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

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

    public void updateProduct(String productName, String productDescription, BigDecimal price, int stock, ProductCategory productTag) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.stock = stock;
        this.productTag = productTag;
    }

    public void addCartItem(CartItem cartItem) {
        if (this.cartItems == null) {
            this.cartItems = new ArrayList<>();
        }
        this.cartItems.add(cartItem);
    }

    public void addReview(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        if (this.reviews != null) {
            this.reviews.remove(review);
        }
    }

    public void addImageUrl(String imageUrl) {
        if (this.imageUrls == null) {
            this.imageUrls = new ArrayList<>();
        }
        this.imageUrls.add(imageUrl);
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = (imageUrls != null) ? new ArrayList<>(imageUrls) : new ArrayList<>();
    }
}