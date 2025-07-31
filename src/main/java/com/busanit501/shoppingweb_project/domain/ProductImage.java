package com.busanit501.shoppingweb_project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "product_image")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "file_name", length = 500, nullable = false)
    private String fileName;

    @Column(name = "ord", nullable = false)
    @Builder.Default
    private int ord = 0;

    @Column(name = "is_thumbnail", nullable = false)
    @Builder.Default
    private boolean thumbnail = false;

    public void changeFileName(String fileName) {
        this.fileName = fileName;
    }

    public void changeOrd(int ord) {
        this.ord = ord;
    }

    public void changeThumbnail(boolean thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}