package com.busanit501.shoppingweb_project.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_detail_image")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetailImage {

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
}