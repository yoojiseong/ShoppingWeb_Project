package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Product;
import com.busanit501.shoppingweb_project.dto.ProductDTO;
import com.busanit501.shoppingweb_project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busanit501.shoppingweb_project.domain.enums.ProductCategory;
import java.util.List;

public interface ProductService {
    // 단건 조회
    ProductDTO getProductById(Long productId);

    // 전체 목록 조회
    List<ProductDTO> getAllProducts();

    // 카테고리별 조회
    List<ProductDTO> getProductsByCategory(String category);

    // 키워드 검색
    List<ProductDTO> searchProducts(String keyword);

    void saveProduct(Product product);
    default Product dtoToEntity(ProductDTO productDTO){
        Product product = Product.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .productTag(productDTO.getProductTag())
                .image(productDTO.getImage())
                .build();
        return product;
    }
    default ProductDTO entityToDto(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .image(product.getImage())
                .productTag(ProductCategory.valueOf(product.getProductTag().name()))
                .build();
        return productDTO;
    }

    // [추가] 새 상품 등록 메서드 시그니처 추가
    ProductDTO createProduct(ProductDTO productDTO);

    // [추가] 상품 수정 메서드 시그니처 추가
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    // [추가] 상품 삭제 메서드 시그니처 추가
    void deleteProduct(Long productId);
}
